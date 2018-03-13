package com.hhu.machinelearningplatformserver.task;

import java.io.Serializable;

import com.hhu.machinelearningplatformserver.data.DataFile;

//任务类
public class SparkTaskInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//任务ID
	private long taskId;
	//任务名称
	private String name;
	//提交用户ID
	private long userId;
	//提交时间
	private long submitTime;
	//任务类型
	private int type;
	//任务算法信息
	private SparkTaskAlgorithm algorithm;
	//任务数据集
	private DataFile dataFile;
	
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(long submitTime) {
		this.submitTime = submitTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public SparkTaskAlgorithm getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(SparkTaskAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	public DataFile getDataFile() {
		return dataFile;
	}
	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}
	
}
