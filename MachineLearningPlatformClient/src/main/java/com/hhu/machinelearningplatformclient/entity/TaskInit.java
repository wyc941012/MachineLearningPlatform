package com.hhu.machinelearningplatformclient.entity;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hhu.machinelearningplatformclient.data.FieldInfo;

public class TaskInit {

	private int taskType;
	private int algorithmId;
	private int modelId;
	private Map<String, String> parameterValue;
	private List<FieldInfo> fieldInfo;
	private int sparkExecutorNum;
	private String dataFileType;
	private String delim;
	private MultipartFile multipartFile;
	
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public int getAlgorithmId() {
		return algorithmId;
	}
	public void setAlgorithmId(int algorithmId) {
		this.algorithmId = algorithmId;
	}
	public int getModelId() {
		return modelId;
	}
	public void setModelId(int modelId) {
		this.modelId = modelId;
	}
	public Map<String, String> getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(Map<String, String> parameterValue) {
		this.parameterValue = parameterValue;
	}
	public List<FieldInfo> getFieldInfo() {
		return fieldInfo;
	}
	public void setFieldInfo(List<FieldInfo> fieldInfo) {
		this.fieldInfo = fieldInfo;
	}
	public int getSparkExecutorNum() {
		return sparkExecutorNum;
	}
	public void setSparkExecutorNum(int sparkExecutorNum) {
		this.sparkExecutorNum = sparkExecutorNum;
	}
	public String getDataFileType() {
		return dataFileType;
	}
	public void setDataFileType(String dataFileType) {
		this.dataFileType = dataFileType;
	}
	public String getDelim() {
		return delim;
	}
	public void setDelim(String delim) {
		this.delim = delim;
	}
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	
}
