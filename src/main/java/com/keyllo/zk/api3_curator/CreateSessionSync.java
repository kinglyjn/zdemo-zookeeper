package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 创建客户端对象
 * @author zhangqingli
 *
 */
public class CreateSessionSync {
	
	public static void main(String[] args) throws InterruptedException {
		// 创建客户端以及常见的3种重试策略
		//RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3, 5000); 
		//RetryPolicy retryPolicy = new RetryNTimes(3, 1000);
		//RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);
		//CuratorFramework client = CuratorFrameworkFactory.newClient("nimbusz:2181", 10000, 10000, retryPolicy);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		// 启动客户端
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
		client.close();
	}
}
