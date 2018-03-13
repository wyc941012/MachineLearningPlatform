package com.hhu.machinelearningplatformclient.task;

import java.io.Serializable;

import com.hhu.machinelearningplatformclient.data.DataFile;



/**
 * Stonk task info
 *
 * @author hayes, @create 2017-12-19 13:40
 **/
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -7755459521939958459L;

    private long taskId;
    
    private TaskType taskType;

    private String name;

    private long userId;

    private DataFile dataFile;
    
    private String workDir;
    
    private int sparkExecutorNum = 1;

    private long timeStamp;

    private SparkTaskAlgorithm sparkTaskAlgorithm;
    
    private int modelId;
    
    public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

    public DataFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(DataFile dataFile) {
		this.dataFile = dataFile;
	}

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public int getSparkExecutorNum() {
		return sparkExecutorNum;
	}

	public void setSparkExecutorNum(int sparkExecutorNum) {
		this.sparkExecutorNum = sparkExecutorNum;
	}

	public SparkTaskAlgorithm getSparkTaskAlgorithm() {
        return sparkTaskAlgorithm;
    }

    public void setSparkTaskAlgorithm(SparkTaskAlgorithm sparkTaskAlgorithm) {
        this.sparkTaskAlgorithm = sparkTaskAlgorithm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

	public int getModelId() {
		return modelId;
	}

	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	
}
