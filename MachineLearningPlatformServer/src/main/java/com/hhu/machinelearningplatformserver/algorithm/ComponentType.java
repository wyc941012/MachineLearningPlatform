package com.hhu.machinelearningplatformserver.algorithm;

public enum ComponentType {

    TRANSFORMER("transformer"),
    ESTIMATOR("estimator");

    private String value;

    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	ComponentType(String value) {
        this.value = value;
    }
    
    public static ComponentType getComponentTypeByValue(String valueStr) {
    	for(ComponentType componentType : ComponentType.values()) {
    		if(componentType.getValue().equals(valueStr)) {
    			return componentType;
    		}
    	}
    	return null;
    }
    
}
