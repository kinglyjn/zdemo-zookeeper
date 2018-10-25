package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 同步删除节点
 * @author zhangqingli
 *
 */
public class DeleteDataSync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new DeleteDataSync());
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
				try {
					zk.delete("/znode01", -1); //版本号-1表示不校验版本号
				} catch (InterruptedException | KeeperException e) {
					e.printStackTrace();
				} 
			}
		}
	}
}
