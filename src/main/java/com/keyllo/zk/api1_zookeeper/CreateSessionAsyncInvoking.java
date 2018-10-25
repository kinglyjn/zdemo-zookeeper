package com.keyllo.zk.api1_zookeeper;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * 创建会话，异步调用
 * @author zhangqingli
 *
 */
public class CreateSessionAsyncInvoking implements Watcher {
	public static ZooKeeper zk;
	private static boolean sthDone = false; //防止网络抖动标志
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new CreateSessionAsyncInvoking());
		System.out.println(zk.getState());  //CONNECTING
		Thread.sleep(Integer.MAX_VALUE);
    }
	
	/**
	 * Watcher process 接口实现
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getState()==KeeperState.SyncConnected) {
			if (!sthDone && event.getType()==EventType.None && event.getPath()==null) {
				//异步创建节点，回调函数为StringCallback.processResult
				zk.create("/znode02", "456".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new StringCallbackImpl(), "这是回调上下文对象");
				sthDone = true;
			}
		}
	}
	
	/**
	 * 回调实现类
	 */
	public static class StringCallbackImpl implements StringCallback {
		/**
		 * rc：返回码，如果异步调用成功，返回0
		 * path：创建节点的路径
		 * ctx：回调上下文
		 * name：服务端创建的已经存在的节点的真实路径
		 */
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println("rc=" + rc);		// rc=0 
			System.out.println("path=" + path);  // path=/znode02
			System.out.println("ctx=" + ctx);    // ctx=这是回调上下文对象
			System.out.println("name=" + name);  // name=/znode02
		}
	}
}
