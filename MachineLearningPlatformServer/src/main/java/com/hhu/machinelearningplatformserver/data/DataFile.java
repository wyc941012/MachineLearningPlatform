package com.hhu.machinelearningplatformserver.data;

import java.io.Serializable;
import java.util.List;

//算法数据集文件
public class DataFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//数据集文件名称
	private String name;
	//数据集文件路径
	private String path;
	//数据集文件上传用户
	private long userId;
	//数据集文件分隔符
	private String delim=",";
	//数据集类型
	private DataFileType dataFileType=DataFileType.CSV;
	//列描述
	private List<FieldInfo> fieldInfos;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getDelim() {
		return delim;
	}
	public void setDelim(String delim) {
		this.delim = delim;
	}
	public DataFileType getDataFileType() {
		return dataFileType;
	}
	public void setDataFileType(DataFileType dataFileType) {
		this.dataFileType = dataFileType;
	}
	public List<FieldInfo> getFieldInfos() {
		return fieldInfos;
	}
	public void setFieldInfos(List<FieldInfo> fieldInfos) {
		this.fieldInfos = fieldInfos;
	}

}
