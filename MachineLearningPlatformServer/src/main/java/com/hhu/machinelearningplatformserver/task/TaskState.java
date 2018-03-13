package com.hhu.machinelearningplatformserver.task;

//任务状态
public enum TaskState {

	INITING(1),
	SUBMITTING(2),
	RUNNING(3),
	SUCCESS(4),
	FAIL(5);
	
	private int value;
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private TaskState(int value) {
		
	}
	
}
