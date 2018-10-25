package com.keyllo.zk.api2_zkclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 创建客户端连接
 * @author zhangqingli
 *
 */
public class CreateSession {
	
	public static void main(String[] args) {
		ZkClient zk = new ZkClient("nimbusz:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("连接成功！");
		
		//创建节点
		User user = new User(1, "张三", true, new ArrayList<>(Arrays.asList(new String[]{"music","painting","pingpong"})));
		String path = zk.create("/znode01", user, CreateMode.EPHEMERAL);
		System.out.println("path=" + path);
		
		//获取节点的数据
		Stat stat = new Stat();
		user = zk.readData(path, stat);
		System.out.println("user=" + user);
		System.out.println("stat=" + stat);
		
		//获取子节点信息
		List<String> children = zk.getChildren("/");
		System.out.println(children);
		
		//检测节点是否存在
		boolean exists = zk.exists("/znode01");
		System.out.println("exists=" + exists);
		
		//节点的删除
		boolean delete = zk.delete("/znode01", -1);
		System.out.println("delete=" + delete);
		boolean deleteRecursive = zk.deleteRecursive("/znode01");
		System.out.println("deleteRecursive=" + deleteRecursive);
		
	}
}
