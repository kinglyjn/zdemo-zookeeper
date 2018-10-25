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
 * 同步获取节点是否存在信息
 * @author zhangqingli
 *
 */
public class NodeExistsSync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new NodeExistsSync());
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
					Stat stat = zk.exists("/znode01", true); //注意只是注册了/znode01的节点，其他节点的创建更改删除不能监听到
					System.out.println(stat);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (event.getType()==EventType.NodeCreated) {
					System.out.println("创建节点：" + event.getPath());
					try {
						System.out.println(zk.exists(event.getPath(), true));
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				} else if (event.getType()==EventType.NodeDataChanged) {
					System.out.println("节点数据改变：" + event.getPath());
					try {
						System.out.println(zk.exists(event.getPath(), true));
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				} else if (event.getType()==EventType.NodeDeleted) {
					System.out.println("节点删除：" + event.getPath());
					try {
						System.out.println(zk.exists(event.getPath(), true));
					} catch (KeeperException | InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
