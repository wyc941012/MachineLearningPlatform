package com.hhu.machinelearningplatformserver.task;

public enum TaskType {

    ESTIMATOR_TYPE(1),
	TRANSFORMER_TYPE(2);

	private int value;
	
    public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private TaskType(int value) {
		
    }
	
}
