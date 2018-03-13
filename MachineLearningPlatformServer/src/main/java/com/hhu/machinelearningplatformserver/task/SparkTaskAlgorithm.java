package com.hhu.machinelearningplatformserver.task;

import java.io.Serializable;
import java.util.Map;

//任务算法信息
public class SparkTaskAlgorithm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//算法ID
	private int id;
	//算法参数名称及值
	private Map<String, String> parameters;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

}
