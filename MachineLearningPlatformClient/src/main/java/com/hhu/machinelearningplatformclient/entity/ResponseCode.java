package com.hhu.machinelearningplatformclient.entity;

public enum ResponseCode {

	SUCCESS("成功", 200), ERROR("错误", 201);
	
	private String name;
	private int value;
	
	private ResponseCode(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
		
}
