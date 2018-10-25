package com.keyllo.zk.api3_curator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 判断节点是否存在(异步)
 * @author zhangqingli
 *
 */
public class CheckExistsASync {

	public static void main(String[] args) throws Exception {
		CuratorFramework zk = CuratorFrameworkFactory.builder().connectString("nimbusz:2181")
									.sessionTimeoutMs(10000)
									.connectionTimeoutMs(10000)
									.retryPolicy(new ExponentialBackoffRetry(1000, 3, 5000))
									.build();
		zk.start();
		
		
		//初始化线程池
		ExecutorService es = Executors.newFixedThreadPool(5); 
		
		//判断节点是否存在，如果节点存在则返回stat对象，否则返回null
		zk.checkExists()
				.inBackground(new BackgroundCallbackImpl(), "这是异步调用的上下文对象", es)
				.forPath("/znode01");
		
		
		Thread.sleep(Integer.MAX_VALUE);
		zk.close();
		es.shutdown();
	}
	
	/**
	 * 回调实现类
	 */
	static class BackgroundCallbackImpl implements BackgroundCallback {
		@Override
		public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
			System.out.println("客户端对象zk：" + client);
			System.out.println("事件对象：" + event);
		}
	}
}
