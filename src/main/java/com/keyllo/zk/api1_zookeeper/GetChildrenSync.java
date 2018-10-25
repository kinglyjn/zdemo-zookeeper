package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import java.util.List;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 同步获取子节点
 * @author zhangqingli
 *
 */
public class GetChildrenSync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new GetChildrenSync());
		System.out.println(zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	/**
	 * watcher实现类
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getState()==KeeperState.SyncConnected) {
			/**
			 * event包含三部分信息：
			 * keeperState：当前zk的状态
			 * eventType：当前事件的时间类型
			 * path：触发这个事件相关联的数据节点
			 */
			if (event.getType()==EventType.None && event.getPath()==null) { //表示客户端与服务器建立连接，这个分支下的代码纸杯执行一次
				try {
					/**
					 * path：该节点路径
					 * watch：对该节点下子节点的变化感不感兴趣（要不要注册该节点的事件监听器）
					 */
					List<String> children = zk.getChildren("/", true);
					System.out.println(children);	//[znode01, storm, zookeeper]
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (event.getType()==EventType.NodeChildrenChanged) { //表示该节点的子节点发生变化
					try {
						/**
						 * zk的事件监听器只能使用一次，如果要反复使用，就需要反复注册事件监听器
						 */
						System.out.println(zk.getChildren(event.getPath(), true)); //[storm, zookeeper]
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
