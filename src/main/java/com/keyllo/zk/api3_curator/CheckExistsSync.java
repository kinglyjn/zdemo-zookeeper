package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * 判断节点是否存在
 * @author zhangqingli
 *
 */
public class CheckExistsSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//判断节点是否存在，如果节点存在则返回stat对象，否则返回null
		Stat stat = zk.checkExists()
			.forPath("/znode01");
		System.out.println("stat=" + stat);
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
