package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步更新数据
 * @author zhangqingli
 *
 */
public class SetDataASync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new SetDataASync());
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
				zk.setData("/znode01", "abcd".getBytes(), -1, new StatCallbackImpl(), "这是回调上下文对象");
			}
		}
	}
	
	/**
	 * 异步调用实现类
	 */
	static class StatCallbackImpl implements StatCallback {
		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			System.out.println("rc=" + rc);
			System.out.println("path=" + path);
			System.out.println("ctx=" + ctx);
			System.out.println("stat=" + stat);
		}
	}
}
