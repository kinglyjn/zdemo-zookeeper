package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 异步删除节点
 * @author zhangqingli
 *
 */
public class DeleteDataASync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new DeleteDataASync());
		System.out.println(zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	/**
	 * watcher实现类
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getState()==KeeperState.SyncConnected) {
			if (event.getType()==EventType.None && event.getPath()==null) { //表示客户端与服务器建立连接，这个分支下的代码只被执行一次
				zk.delete("/znode01", -1, new VoidCallbackImpl(), "这是回调上下文对象");
			}
		}
	}
	
	/**
	 * 回调实现类
	 */
	static class VoidCallbackImpl implements VoidCallback {
		@Override
		public void processResult(int rc, String path, Object ctx) {
			System.out.println("rc=" + rc);		// rc=0 
			System.out.println("path=" + path);  // path=/
			System.out.println("ctx=" + ctx);    // ctx=这是回调上下文对象
		}
	}
}
