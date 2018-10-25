package com.keyllo.zk.api1_zookeeper;

import java.io.IOException;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 同步获取节点数据
 * @author zhangqingli
 *
 */
public class GetDataSync implements Watcher {
	private static ZooKeeper zk;
	private static Stat stat = new Stat(); //表示一个节点的状态信息
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new GetDataSync());
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
					byte[] data = zk.getData("/znode01", true, stat);
					System.out.println(new String(data)); //
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (event.getType()==EventType.NodeDataChanged) { //表示该节点的数据内容发生变化
					try {
						System.out.println(new String(zk.getData(event.getPath(), true, stat))); //
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
