package com.hhu.machinelearningplatformclient.data;

public enum DataFileType {
    CSV("csv"),
    LIBSVM("libsvm");

    private String type;

    DataFileType(String type) {
        this.type = type;
    }
    
    public static DataFileType getDataFileTypeByValue(String value) {
    	for(DataFileType dataFileType : DataFileType.values()) {
    		if(dataFileType.name().equals(value)) {
    			return dataFileType;
    		}
    	}
    	return null;
    }
    
}
