package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 删除节点
 * @author zhangqingli
 *
 */
public class DeleteNodeSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		// 删除节点
		zk.delete()
			.guaranteed() //只要客户端会话有效，curator会在后台进行持续的删除，以保证删除成功
			.deletingChildrenIfNeeded()
			.withVersion(-1)
			.forPath("/znode01");
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
