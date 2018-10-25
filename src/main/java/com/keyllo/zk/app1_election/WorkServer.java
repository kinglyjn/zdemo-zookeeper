package com.keyllo.zk.app1_election;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

/**
 * 主工作类
 * 
 * master节点被删除的情况包含三种：
 * 1. master节点主动释放master权利
 * 2. master节点宕机
 * 3. 网络抖动
 *
 */
public class WorkServer {
	
	private volatile boolean running = false; 			//记录服务器的状态
	private ZkClient zk;									//zkclient客户端
	private static final String MASTER_PATH = "/master";	//记录master节点路径
	private IZkDataListener dataListener;				//监听master节点的删除事件
	private RunningData serverData;						//用于记录当前集群中slave节点的基本信息
	private RunningData masterData;						//用于记录当前集群中master节点的基本信息
	
	//延迟器
	private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);
	//延迟时间为5s
	private long delayTime = 5;
	
	
	public ZkClient getZk() {
		return zk;
	}
	public void setZk(ZkClient zk) {
		this.zk = zk;
	}


	public WorkServer(RunningData rd) {
		this.serverData = rd;
		this.dataListener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				//某台服务器宕机，发起争抢master节点的操作
				//如果当前节点是master，立即争抢master；否则延迟5s争抢master（防止网络抖动）
				if (masterData!=null && masterData.getName().equals(serverData.getName())) {
					takeMaster();
				} else {
					delayExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							takeMaster();
						}
					}, delayTime, TimeUnit.SECONDS);
				}
			}
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO
			}
		};
	}
	
	
	//启动服务器
	public void start() throws Exception {
		//首先判断当前服务器是否在运行，如果已经在运行则抛出异常
		if (running) {
			throw new RuntimeException("server has startup！");
		}
		running = true;
		
		//订阅master节点的删除事件
		zk.subscribeDataChanges(MASTER_PATH, dataListener);
		
		//争抢master
		takeMaster();
	}
	
	
	//停止服务器
	public void stop() throws Exception {
		//首先判断当前服务器是否已停止，如果已停止则抛出异常
		if (!running) {
			throw new RuntimeException("server has stoped！");
		} 
		running = false;
		
		////关闭线程池
		delayExecutor.shutdown();
		
		//取消master节点的事件订阅
		zk.unsubscribeDataChanges(MASTER_PATH, dataListener);
		
		//释放master权利
		releaseMaster();
	}
	
	//争抢master权利
	public void takeMaster() {
		//首先判断当前服务器是否已停止，如果已停止则直接返回，否则takeMaster
		if (!running) {
			return;
		}
		
		try {
			//创建master节点，并将serverData赋值给masterData
			zk.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
			masterData = serverData;
			System.out.println(serverData.getName()+" is master");
			
			
			/**
			 * 作为演示，我们让服务器每隔5s释放一次master
			 */
			delayExecutor.schedule(new Runnable() {
				@Override
				public void run() {
					if (checkMaster()) {
						releaseMaster();
					}
				}
			}, delayTime, TimeUnit.SECONDS);
		} catch (ZkNodeExistsException e) {
			RunningData runningData = zk.readData(MASTER_PATH, true);
			if (runningData==null) {	//如果没有读取到master节点数据，说明master节点在读取的瞬间宕机了
				takeMaster();
			} else {
				masterData = runningData;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ZkException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//释放master权利
	public void releaseMaster() {
		//首先判断当前自己是不是master，若是才删除master节点
		if (checkMaster()) {
			zk.delete(MASTER_PATH);
		}
	}
	
	//检测自己是不是master
	public boolean checkMaster() {
		try {
			RunningData runningData = zk.readData(MASTER_PATH, true);
			masterData = runningData;
			
			//如果masterData的名称和serverData的名称一致，则认为自己是master
			if (masterData.getName().equals(serverData.getName())) {
				return true;
			}
		} catch (ZkNoNodeException e) {
			//节点不存在
			return false;
		} catch (ZkInterruptedException e) {
			//中断异常，重试
			return checkMaster();
		} catch (ZkException e) {
			//剩余异常
			return false;
		}
		return false;
	}
}
