package com.keyllo.zk.app1_election;

import java.io.Serializable;

/**
 * 描述workServer的基本信息
 * @author zhangqingli
 *
 */
public class RunningData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long cid; 	//记录服务器id
	private String name;	//记录服务器名称
	
	
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
