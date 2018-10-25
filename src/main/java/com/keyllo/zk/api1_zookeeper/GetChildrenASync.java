package com.keyllo.zk.api1_zookeeper;

import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * 异步获取子节点
 * @author zhangqingli
 *
 */
public class GetChildrenASync implements Watcher {
	private static ZooKeeper zk;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zk = new ZooKeeper("nimbusz:2181", 5000, new GetChildrenASync());
		System.out.println(zk.getState());
		Thread.sleep(Integer.MAX_VALUE);
	}

	/**
	 * watcher process 实现接口
	 */
	@Override
	public void process(WatchedEvent event) {
		if (event.getState()==KeeperState.SyncConnected) {
			if (event.getType()==EventType.None && event.getPath()==null) {
				zk.getChildren("/", true, new ChildrenCallbackImpl(), "这是异步调用上下文");
			} else {
				if (event.getType()==EventType.NodeChildrenChanged) { 
					try {
						// 再次注册事件
						zk.getChildren(event.getPath(), true, new ChildrenCallbackImpl(), "这是异步调用上下文");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 回调实现类
	 */
	static class ChildrenCallbackImpl implements Children2Callback {
		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) { //stat表示节点的状态
			System.out.println("rc=" + rc);		// rc=0 
			System.out.println("path=" + path);  // path=/
			System.out.println("ctx=" + ctx);    // ctx=这是回调上下文对象
			System.out.println("children=" + children);  // children=[storm, zookeeper]
			System.out.println("stat=" + stat);	// stat=0,0,0,0,0,54,0,0,0,2,4483945857210
		}
	}
}
