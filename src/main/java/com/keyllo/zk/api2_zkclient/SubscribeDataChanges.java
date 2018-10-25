package com.keyllo.zk.api2_zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

/**
 * 订阅子节点数据内容的变化
 * @author zhangqingli
 *
 */
public class SubscribeDataChanges {
	
	public static void main(String[] args) throws InterruptedException {
		ZkClient zk = new ZkClient("nimbusz:2181", 10000, 10000, new BytesPushThroughSerializer()); ////注意这里的序列化器
		System.out.println("连接成功！");
		
		// 订阅子节点的变化
		zk.subscribeDataChanges("/znode01", new ZkDataListenerImpl());
		Thread.sleep(Integer.MAX_VALUE);
	}
	
	
	/**
	 * 回调实现类
	 */
	static class ZkDataListenerImpl implements IZkDataListener {
		/**
		 * 当该节点创建和数据改变的时候会被触发
		 */
		@Override
		public void handleDataChange(String dataPath, Object data) throws Exception {
			System.out.println("----handleDataChange----");
			System.out.println("dataPath=" + dataPath);
			System.out.println("data=" + new String((byte[])data));
		}
		
		/**
		 * 当该节点被删除的时候会触发
		 */
		@Override
		public void handleDataDeleted(String dataPath) throws Exception {
			System.out.println("----handleDataDeleted----");
			System.out.println("dataPath=" + dataPath);
		}
	}
}
