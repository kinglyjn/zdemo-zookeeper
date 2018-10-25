package com.keyllo.zk.api2_zkclient;

import java.util.ArrayList;
import java.util.Arrays;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 创建客户端连接
 * @author zhangqingli
 *
 */
public class WriteData {
	
	public static void main(String[] args) {
		ZkClient zk = new ZkClient("nimbusz:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("连接成功！");
		
		// 创建节点
		User user = new User(1, "张三", true, new ArrayList<>(Arrays.asList(new String[]{"music","painting","pingpong"})));
		String path = zk.create("/znode01", user, CreateMode.EPHEMERAL);
		System.out.println("path=" + path);
		
		// 获取节点
		user = zk.readData("/znode01", true);
		System.out.println("user=" + user);
		
		// 修改节点
		user.setName("小娟");
		user.setGender(false);
		Stat stat = zk.writeDataReturnStat("/znode01", user, -1);
		System.out.println("stat=" + stat);
		
		// 再次获取节点
		user = zk.readData("/znode01", true);
		System.out.println("user=" + user);
	}
}
