package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * 获取子节点列表
 * @author zhangqingli
 *
 */
public class GetDataSync2 {

	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder()
									.connectString("192.168.1.82:6200,192.168.1.100:6200,192.168.1.229:6200")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		client.start();
		
		//获取子节点列表
		Stat stat = new Stat();
		byte[] ret = client.getData().storingStatIn(stat).forPath("/sqoopui");
		System.out.println(new String(ret));
		System.out.println(stat);
		
		Thread.sleep(Integer.MAX_VALUE);
		client.close();
	}
}
