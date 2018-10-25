package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 创建会话，同步调用
 * @author zhangqingli
 *
 */
public class CreateSessionSyncInvoking implements Watcher {
	public static ZooKeeper zk;
	private static boolean sthDone = false; //防止网络抖动标志
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new CreateSessionSyncInvoking());
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
				try {
					//同步创建节点
					String path = zk.create("/znode01", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT); //CreateMode表示[-s -e]选项
					System.out.println(path);  //znode01
					sthDone = true;
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
