package com.hhu.machinelearningplatformclient.model;

//算法模型类
public class AlgorithmModel {

	private int id;
	private String name;
	private int userId;
	private long createTime;
	private String path;
	private int algorithmId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getAlgorithmId() {
		return algorithmId;
	}
	public void setAlgorithmId(int algorithmId) {
		this.algorithmId = algorithmId;
	}
	
}
