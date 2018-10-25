package com.keyllo.zk.api1_zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 同步更新数据
 * @author zhangqingli
 *
 */
public class CreateNodeWithAuthSync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws Exception {
		zk = new ZooKeeper("nimbusz:2181", 5000, new CreateNodeWithAuthSync());
		System.out.println(zk.getState());
		//zk.create("/banzhang", "mydata".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
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
					ACL ipAcl = new ACL(Perms.CREATE|Perms.READ|Perms.WRITE, new Id("ip", "172.16.127.129"));
					ACL digestAcl = new ACL(Perms.CREATE|Perms.READ|Perms.WRITE|Perms.DELETE|Perms.ADMIN, 
							new Id("digest", DigestAuthenticationProvider.generateDigest("super:123456")));
					List<ACL> acls = new ArrayList<ACL>();
					acls.add(ipAcl);
					acls.add(digestAcl);
					
					String path = zk.create("/znode01", "123456".getBytes(), acls, CreateMode.PERSISTENT);
					System.out.println("path: " + path);  //path: /znode01
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
