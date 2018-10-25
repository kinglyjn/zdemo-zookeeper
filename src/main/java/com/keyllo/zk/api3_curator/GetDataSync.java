package com.keyllo.zk.api3_curator;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 获取子节点列表
 * @author zhangqingli
 *
 */
public class GetDataSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//获取子节点列表
		List<String> children = zk.getChildren().forPath("/");
		System.out.println("children=" + children);
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
