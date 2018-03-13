package com.hhu.machinelearningplatformclient.algorithm;

/**
 * 参数值的类型
 *
 * @author hayes, @create 2017-12-11 19:43
 **/
public enum ParameterValueType {

    INT("int"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    STRING("string");

    private String valueType;

    ParameterValueType(String valueType) {
        this.valueType = valueType;
    }


}
