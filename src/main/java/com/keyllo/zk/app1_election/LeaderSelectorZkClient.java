package com.keyllo.zk.app1_election;

import java.util.ArrayList;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 作为调度器启动和停止workServer
 * @author zhangqingli
 *
 */
public class LeaderSelectorZkClient {
	private static final int CLIENT_QTY = 10;					//模拟10个服务器
	private static final String ZK_SERVER = "nimbusz:2181";	//zk服务器
	
	public static void main(String[] args) throws Exception {
		ArrayList<ZkClient> zks = new ArrayList<>();			//保存所有zkclient的列表
		ArrayList<WorkServer> workServers = new ArrayList<>();//保存所有服务的列表
		
		try {
			for (int i = 0; i < CLIENT_QTY; i++) {
				//创建zkclient
				ZkClient zk = new ZkClient(ZK_SERVER, 5000, 5000, new SerializableSerializer());
				zks.add(zk);
				
				//创建serverData
				RunningData runningData = new RunningData();
				runningData.setCid(Long.valueOf(i));
				runningData.setName("Client#" + i);
				
				//创建服务
				WorkServer workServer = new WorkServer(runningData);
				workServer.setZk(zk);
				workServers.add(workServer);
				workServer.start();
			}
			
			Thread.sleep(Integer.MAX_VALUE);
		} finally {
			System.out.println("shutting down...");
			for (WorkServer workServer : workServers) {
				workServer.stop();
			}
			for (ZkClient zk : zks) {
				zk.close();
			}
		}
	}
}
