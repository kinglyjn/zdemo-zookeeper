package com.keyllo.zk.api3_curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 监听子节点
 * @author zhangqingli
 *
 */
public class NodeChildrenListener {
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
		
		//监听子节点
		//cacheData为true表示系统在监听到子节点列表发生变化时会同时获取子节点的数据内容
		final PathChildrenCache pathChildrenCache = new PathChildrenCache(zk, "/znode01", true);
		pathChildrenCache.start();  //开启监听
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
					case CHILD_ADDED: //添加子节点
						System.out.println("CHILD_ADDED: " + event.getData());
						break;
					case CHILD_UPDATED: //子节点内容发生改变
						System.out.println("CHILD_UPDATED: " + event.getData());
						break;
					case CHILD_REMOVED: //删除子节点
						System.out.println("CHILD_REMOVED: " + event.getData());
						break;

					default:
						break;
				}
			}
		}, es);
		
		
		Thread.sleep(Integer.MAX_VALUE);
		pathChildrenCache.close();
		zk.close();
		es.shutdown();
	}
}
