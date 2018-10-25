package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 创建节点
 * @author zhangqingli
 *
 */
public class CreateNodeSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//创建节点
		String path = zk.create().creatingParentsIfNeeded()
				.withMode(CreateMode.EPHEMERAL)
				.forPath("/znode01/znode01_01", "1234567".getBytes());
		System.out.println("path=" + path);
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
