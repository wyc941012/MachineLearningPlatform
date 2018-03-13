package com.hhu.machinelearningplatformserver.data;

public enum DataFileType {
    CSV("csv"),
    LIBSVM("libsvm");

    private String type;

    DataFileType(String type) {
        this.type = type;
    }
}
