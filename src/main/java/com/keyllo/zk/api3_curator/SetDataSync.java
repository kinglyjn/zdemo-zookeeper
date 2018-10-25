package com.keyllo.zk.api3_curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

/**
 * 更新节点数据
 * @author zhangqingli
 *
 */
public class SetDataSync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//获取数据的版本信息
		Stat stat = new Stat();
		zk.getData()
			.storingStatIn(stat)
			.forPath("/znode01");
		System.out.println("当前数据版本：" + stat.getVersion());
		
		
		//更新节点数据
		stat = zk.setData()
			.withVersion(stat.getVersion())	
			.forPath("/znode01", "abc".getBytes());
		System.out.println("更新后数据版本：" + stat.getVersion());
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
	}
}
