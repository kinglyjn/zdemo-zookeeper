package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * 获取节点数据内容
 * @author zhangqingli
 *
 */
public class GetChildrenSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//获取节点数据内容
		Stat stat = new Stat();
		byte[] dataBytes = zk.getData()
			.storingStatIn(stat)
			.forPath("/znode01");
		System.out.println("data=" + new String(dataBytes));
		System.out.println("stat=" + stat);
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
