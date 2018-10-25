package com.keyllo.zk.api2_zkclient;

import java.util.List;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 订阅子节点的变化
 * 当创建和删除该节点、创建和删除该节点下的子节点会被触发
 * 但是当修改当前节点会修改当前节点下的子节点时并不会被触发
 * @author zhangqingli
 *
 */
public class SubscribeChildChanges {
	
	public static void main(String[] args) throws InterruptedException {
		ZkClient zk = new ZkClient("nimbusz:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("连接成功！");
		
		// 订阅子节点的变化
		zk.subscribeChildChanges("/znode01", new ZkChildListenerImpl());
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	
	/**
	 * 回调实现类
	 */
	static class ZkChildListenerImpl implements IZkChildListener {
		@Override
		public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
			System.out.println("parentPath=" + parentPath);
			System.out.println("currentChilds=" + currentChilds);
		}
	}
}
