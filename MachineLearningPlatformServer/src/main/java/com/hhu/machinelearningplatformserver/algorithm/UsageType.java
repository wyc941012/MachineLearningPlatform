package com.hhu.machinelearningplatformserver.algorithm;

public enum UsageType {

    CLUSTERING("clustering"),
    REGRESSION("regression"),
    CLASSIFICATION("classification"),
	RECOMMENDATION("recommendation");

    private String value;

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	UsageType(String value) {
        this.value = value;
    }
    
    public static UsageType getUsageTypeByValue(String valueStr) {
    	for(UsageType usageType : UsageType.values()) {
    		if(usageType.getValue().equals(usageType.getValue())) {
    			return usageType;
    		}
    	}
    	return null;
    }
    
}
