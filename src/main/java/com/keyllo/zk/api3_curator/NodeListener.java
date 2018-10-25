package com.keyllo.zk.api3_curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听节点
 * @author zhangqingli
 *
 */
public class NodeListener {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder()
				.connectString("nimbusz:2181")
				.sessionTimeoutMs(10000)
				.connectionTimeoutMs(10000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
				.build();
		zk.start();
		
		
		//初始化线程池
		ExecutorService es = Executors.newFixedThreadPool(5); 
		
		//监听节点的创建和内容修改
		final NodeCache nodeCache = new NodeCache(zk, "/znode01");
		nodeCache.start(); //开启对该节点的监听
		nodeCache.getListenable().addListener(new NodeCacheListener() {
			@Override
			public void nodeChanged() throws Exception {
				byte[] data = nodeCache.getCurrentData().getData();
				System.out.println("newData: " + new String(data));
			}
		}, es);
		
		Thread.sleep(Integer.MAX_VALUE);
		nodeCache.close();
		zk.close();
		es.shutdown();
	}
	
}
