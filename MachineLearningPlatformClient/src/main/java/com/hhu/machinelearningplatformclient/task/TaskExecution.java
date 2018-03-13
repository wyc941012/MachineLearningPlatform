package com.hhu.machinelearningplatformclient.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hhu.machinelearningplatformclient.common.HBaseUtil;
import com.hhu.machinelearningplatformclient.common.HDFSUtils;
import com.hhu.machinelearningplatformclient.common.RedisUtils;
import com.hhu.machinelearningplatformclient.data.DataFile;
import com.hhu.machinelearningplatformclient.data.DataFileType;
import com.hhu.machinelearningplatformclient.data.FieldInfo;
import com.hhu.machinelearningplatformclient.entity.TaskInit;

@Component
public class TaskExecution {

	@Resource
	private RedisUtils redisUtils;
	@Resource
	private HDFSUtils hdfsUtils;
	@Resource
	private SparkTaskExecutor sparkTaskExecutor;
	
	//任务初始化
	public void taskInit(TaskInit taskInit) {
		TaskInfo taskInfo=new TaskInfo();
		long taskId=new Random().nextLong();
		redisUtils.put(taskId, TaskState.INITING.getValue());
		int taskType=taskInit.getTaskType();
		//上传数据集文件到HDFS
		Map<String, String> parameterValue=taskInit.getParameterValue();
		List<FieldInfo> fieldInfo=taskInit.getFieldInfo();
		int sparkExecutorNum=taskInit.getSparkExecutorNum();
		String dataFileType=taskInit.getDataFileType();
		String delim=taskInit.getDelim();
		MultipartFile multipartFile=taskInit.getMultipartFile();
		String fileName=multipartFile.getName();
		//创建任务工作目录
		String taskDir="task-"+taskId+"/";
		String taskInputDir=taskDir+"input/";
		String taskOutputDir=taskDir+"output/";
		String taskModelDir=taskDir+"model/";
		try {
			hdfsUtils.createDirectory(taskDir);
			hdfsUtils.createDirectory(taskInputDir);
			hdfsUtils.createDirectory(taskOutputDir);
			hdfsUtils.createDirectory(taskModelDir);
			InputStream inputStream=multipartFile.getInputStream();
			hdfsUtils.uploadFileStream(true, inputStream, taskInputDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			redisUtils.put(taskId, TaskState.FAIL.getValue());
		}	
		DataFile dataFile=new DataFile();
		dataFile.setName(fileName);
		dataFile.setUserId(1);
		dataFile.setPath(taskInputDir+fileName);
		dataFile.setDataFileType(DataFileType.getDataFileTypeByValue(dataFileType));
		dataFile.setDelim(delim);
		dataFile.setFieldInfos(fieldInfo);
		if(taskType==TaskType.ESTIMATOR_TYPE.getValue()) {
			taskInfo.setTaskType(TaskType.ESTIMATOR_TYPE);
			int algorithmId=taskInit.getAlgorithmId();
			SparkTaskAlgorithm sparkTaskAlgorithm=new SparkTaskAlgorithm();
			sparkTaskAlgorithm.setId(algorithmId);
			sparkTaskAlgorithm.setParameters(parameterValue);
			taskInfo.setSparkTaskAlgorithm(sparkTaskAlgorithm);
		}
		if(taskType==TaskType.TRANSFORMER_TYPE.getValue()) {
			taskInfo.setTaskType(TaskType.TRANSFORMER_TYPE);
			int modelId=taskInit.getModelId();
			taskInfo.setModelId(modelId);
		}
		//生成任务类
		taskInfo.setTaskId(taskId);
		taskInfo.setName("任务"+taskId);
		taskInfo.setUserId(1);
		taskInfo.setDataFile(dataFile);
		taskInfo.setWorkDir(taskDir);
		taskInfo.setSparkExecutorNum(sparkExecutorNum);
		taskInfo.setTimeStamp(System.currentTimeMillis());
		try {
			//写入任务信息到HBase
			HBaseUtil hBaseUtil=HBaseUtil.getInstance();
			hBaseUtil.connection();
			ObjectMapper objectMapper=new ObjectMapper();
			hBaseUtil.putData("task", String.valueOf(taskId), "info", "taskInfo", objectMapper.writeValueAsString(taskInfo));
			hBaseUtil.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			redisUtils.put(taskId, TaskState.FAIL.getValue());
		}
		taskSubmit(taskInfo);
	}
	
	//任务提交
	private void taskSubmit(TaskInfo taskInfo) {
		redisUtils.put(taskInfo.getTaskId(), TaskState.SUBMITTING.getValue());
		try {
			sparkTaskExecutor.execute(taskInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			redisUtils.put(taskInfo.getTaskId(), TaskState.FAIL.getValue());
		}
	}
	
}
