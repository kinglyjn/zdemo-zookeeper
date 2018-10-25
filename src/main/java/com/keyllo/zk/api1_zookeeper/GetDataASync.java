package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 异步获取节点数据
 * @author zhangqingli
 *
 */
public class GetDataASync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new GetDataASync());
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
				zk.getData("/znode01", true, new DataCallbackImpl(), "这是回调上下文对象");
			} else {
				if (event.getType()==EventType.NodeDataChanged) { //表示该节点的数据内容发生变化
					zk.getData("/znode01", true, new DataCallbackImpl(), "这是回调上下文对象");
				}
			}
		}
	}
	
	/**
	 * 回调实现类
	 */
	static class DataCallbackImpl implements DataCallback {
		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			System.out.println("rc=" + rc);		// rc=0 
			System.out.println("path=" + path);  // path=/znode01
			System.out.println("ctx=" + ctx);    // ctx=这是回调上下文对象
			System.out.println("data=" + new String(data));  // data=1234567
			System.out.println("stat=" + stat);	// stat=4483945857217,4483945857219,1501059675650,1501060140309,1,0,0,0,7,0,4483945857217
		}
	}
}
