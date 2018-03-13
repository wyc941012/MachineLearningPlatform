package com.hhu.machinelearningplatformserver.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhu.machinelearningplatformserver.common.HBaseUtil;

/**
 * DataFile Mapper
 *
 * @author hayes, @create 2017-12-20 16:08
 **/
public class DataFileMapper {

    private static final String TABLE_NAME = "task";

    private static final String FAMILY_NAME = "info";

    private static final String JSON_QUALIFIER_NAME = "json";

    private HBaseUtil hBaseUtil;

    private ObjectMapper JSON;

    public DataFileMapper() throws IOException {
    	hBaseUtil=HBaseUtil.getInstance();
        JSON = new ObjectMapper();
    }

    /**
     * 添加数据
     *
     * @param dataFile
     * @throws Exception
     */
    public void put(DataFile dataFile) throws Exception {
        String rowKey = buildRowKey(dataFile.getUserId(), dataFile.getName());

        hBaseUtil.putData(TABLE_NAME, rowKey, FAMILY_NAME, JSON_QUALIFIER_NAME, JSON.writeValueAsString(dataFile));
    }

    /**
     * 查询
     *
     * @param uname
     * @param dataFileName
     * @return
     * @throws Exception
     */
    public DataFile get(long userId, String dataFileName) throws Exception {
        String rowKey = buildRowKey(userId, dataFileName);

        String dataFileJson = hBaseUtil.getValue(TABLE_NAME, rowKey, FAMILY_NAME, JSON_QUALIFIER_NAME);
        return JSON.readValue(dataFileJson, DataFile.class);
    }

    /**
     * 查询用户的所有数据文件
     *
     * @param uname
     * @return
     * @throws Exception
     */
    public List<DataFile> get(String uname) throws Exception {
        List<String> dataFileJsons = hBaseUtil.getValueByRowPrefix(TABLE_NAME, uname, FAMILY_NAME, JSON_QUALIFIER_NAME);
        List<DataFile> dataFiles = new ArrayList<DataFile>();
        for (String json : dataFileJsons) {
            dataFiles.add(JSON.readValue(json, DataFile.class));
        }
        return dataFiles;
    }


    /**
     * 生成rowkey  {uname}-{dataFile.name}
     *
     * @param uname
     * @param dataFileName
     * @return
     */
    private String buildRowKey(long userId, String dataFileName) {
        return userId + "_" + dataFileName;
    }

}
