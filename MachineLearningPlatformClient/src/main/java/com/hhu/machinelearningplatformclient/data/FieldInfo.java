package com.hhu.machinelearningplatformclient.data;

import java.io.Serializable;


/**
 * 列属性描述
 *
 * @author hayes, @create 2017-12-11 19:02
 **/
public class FieldInfo implements Serializable {

    private static final long serialVersionUID = -7123058551214352633L;

    public static final String DOUBLE_DATATYPE = "double";
    public static final String BOOLEAN_DATATYPE = "boolean";
    public static final String INTEGER_DATATYPE = "int";
    public static final String STRING_DATATYPE = "string";
    public static final String TIMESTAMP_DATATYPE = "timestamp";
    public static final String LONG_DATATYPE = "long";
    public static final String NULL_DATATYPE = "null";


    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 是否可以为空
     */
    private boolean nullable;

    /**
     * index(-1为多列)
     */
    private int index = -1;

    /**
     * Start index
     */
    private int startIndex;

    /**
     * end index
     */
    private int endIndex;


    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
