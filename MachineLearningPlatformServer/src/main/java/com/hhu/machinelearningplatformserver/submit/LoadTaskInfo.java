package com.hhu.machinelearningplatformserver.submit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhu.machinelearningplatformserver.common.HBaseUtil;
import com.hhu.machinelearningplatformserver.task.TaskInfo;

/**
 * 加载任务参数信息
 *
 **/
public class LoadTaskInfo {

    private static final String TABLE_NAME = "task";

    private static final String FAMILY_NAME = "info";

    private static final String JSON_QUALIFIER_NAME = "json";

    private HBaseUtil hBaseUtil;

    private ObjectMapper JSON;

    public LoadTaskInfo() throws IOException {
    	hBaseUtil=HBaseUtil.getInstance();
        JSON = new ObjectMapper();
    }

    /**
     * 添加数据
     *
     * @param dataFile
     * @throws Exception
     */
    public void put(TaskInfo taskInfo) throws Exception {
        String rowKey = buildRowKey(taskInfo.getUserId(), taskInfo.getTaskId());

        hBaseUtil.putData(TABLE_NAME, rowKey, FAMILY_NAME, JSON_QUALIFIER_NAME, JSON.writeValueAsString(taskInfo));
    }

    /**
     * 查询
     *
     * @param uname
     * @param dataFileName
     * @return
     * @throws Exception
     */
    public TaskInfo get(long userId, long taskId) throws Exception {
        String rowKey = buildRowKey(userId, taskId);

        String taskJson = hBaseUtil.getValue(TABLE_NAME, rowKey, FAMILY_NAME, JSON_QUALIFIER_NAME);
        return JSON.readValue(taskJson, TaskInfo.class);
    }

    /**
     * 查询用户的所有数据文件
     *
     * @param uname
     * @return
     * @throws Exception
     */
    public List<TaskInfo> get(String uname) throws Exception {
        List<String> taskJsons = hBaseUtil.getValueByRowPrefix(TABLE_NAME, uname, FAMILY_NAME, JSON_QUALIFIER_NAME);
        List<TaskInfo> tasks = new ArrayList<TaskInfo>();
        for (String json : taskJsons) {
        	tasks.add(JSON.readValue(json, TaskInfo.class));
        }
        return tasks;
    }


    /**
     * 生成rowkey  {uname}-{dataFile.name}
     *
     * @param uname
     * @param dataFileName
     * @return
     */
    private String buildRowKey(long userId, long taskId) {
        return userId + "_" + taskId;
    }

}
