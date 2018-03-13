package com.hhu.machinelearningplatformclient.entity;

import com.hhu.machinelearningplatformclient.algorithm.ParameterValueType;

//算法参数
public class Parameter {

    //参数名称
    public String name;

    //参数展示名称
    public String showName;

    //参数类型
    public ParameterValueType valueType;
  

    public Class<?> javaTypeClass() {
        switch (valueType) {
            case BOOLEAN: {
                return boolean.class;
            }
            case STRING: {
                return String.class;
            }
            case INT: {
                return int.class;
            }
            case DOUBLE: {
                return double.class;
            }
        }
        return String.class;
    }

    public Object valueOf(String value) {
        switch (valueType) {
            case BOOLEAN: {
                return Boolean.valueOf(value);
            }
            case STRING: {
                return value;
            }
            case INT: {
                return Integer.valueOf(value);
            }
            case DOUBLE: {
                return Double.valueOf(value);
            }
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public ParameterValueType getValueType() {
        return valueType;
    }

    public void setValueType(ParameterValueType valueType) {
        this.valueType = valueType;
    }
    
}
