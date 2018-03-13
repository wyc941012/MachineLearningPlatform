package com.hhu.machinelearningplatformclient.entity;

import java.util.Map;

//算法实体类
public class MLAlgorithm {

	//算法ID
	private int id;
	//算法名称
	private String name;
	//算法类名
	private String className;
	//算法类型
	private int componentsType;
	//算法用途
	private int userType;
	//算法参数
	private Map<String, Parameter> map;
	
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getComponentsType() {
		return componentsType;
	}
	public void setComponentsType(int componentsType) {
		this.componentsType = componentsType;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public Map<String, Parameter> getMap() {
		return map;
	}
	public void setMap(Map<String, Parameter> map) {
		this.map = map;
	}
	
}
