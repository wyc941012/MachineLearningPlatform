package com.hhu.machinelearningplatformserver.data;

/**
 * a
 *
 * @author hayes, @create 2017-12-20 15:36
 **/

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import com.hhu.machinelearningplatformserver.exception.CantConverException;

import java.util.List;

/**
 * 行->Row
 */
class LineParse implements Function<String, Row> {
    private static final long serialVersionUID = -1481954080127428634L;

    private DataFile dataFile;

    public LineParse(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public Row call(String line) throws Exception {
        String[] strArr;
        if (StringUtils.isEmpty(dataFile.getDelim())) {
            strArr = new String[]{line};
        } else {
            strArr = line.split(dataFile.getDelim());
        }

        List<FieldInfo> fieldInfos = dataFile.getFieldInfos();
        Object[] objs = new Object[fieldInfos.size()];
        for (int i = 0; i < fieldInfos.size(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            //单列
            if (fieldInfo.getIndex() != -1) {
                objs[i] = fieldCall(fieldInfo, strArr[i]);
            //多列
            } else {
                int tmpSize = fieldInfo.getEndIndex() - fieldInfo.getStartIndex() + 1;
                String[] tmp = new String[tmpSize];
                System.arraycopy(strArr, fieldInfo.getStartIndex(), tmp, 0, tmpSize);
                objs[i] = fieldCall(fieldInfo, tmp);
            }
        }
        return RowFactory.create(objs);
    }

    /**
     * String[] -> Obj
     *
     * @param value
     * @return
     * @throws Exception
     */
    public Object fieldCall(FieldInfo info, String[] value) throws Exception {
        switch (info.getDataType()) {
            case FieldInfo.STRING_DATATYPE:
            case FieldInfo.DOUBLE_DATATYPE:
            case FieldInfo.INTEGER_DATATYPE:
            case FieldInfo.LONG_DATATYPE: {
                double[] vect = new double[value.length];
                try {
                    for (int i = 0; i < value.length; i++) {
                        vect[i] = Double.valueOf(value[i]);
                    }
                } catch (Exception e) {
                    throw new CantConverException(e.getMessage());
                }
                return Vectors.dense(vect);
            }
            default:
                throw new CantConverException("不合法类型");
        }
    }

    /**
     * String -> obj
     *
     * @param value
     * @return
     * @throws Exception
     */
    public Object fieldCall(FieldInfo info, String value) throws Exception {
        if (StringUtils.isEmpty(value) && !info.isNullable()) {
            throw new Exception(info.getName() + "列为空");
        } else if (StringUtils.isEmpty(value)) {
            return null;
        }

        try {
            switch (info.getDataType()) {
                case FieldInfo.BOOLEAN_DATATYPE: {
                    return Boolean.valueOf(value);
                }
                case FieldInfo.STRING_DATATYPE: {
                    return value;
                }
                case FieldInfo.DOUBLE_DATATYPE: {
                    return Double.valueOf(value);
                }
                case FieldInfo.INTEGER_DATATYPE: {
                    return Integer.valueOf(value);
                }
                case FieldInfo.LONG_DATATYPE: {
                    return Long.valueOf(value);
                }
                case FieldInfo.TIMESTAMP_DATATYPE: {
                    return Long.valueOf(value);
                }
                case FieldInfo.NULL_DATATYPE: {
                    return null;
                }
                default: {
                    throw new CantConverException("dataType不支持");
                }
            }
        } catch (Exception e) {
            throw new Exception(value + "->" + info.getDataType() + " error");
        }

    }
}