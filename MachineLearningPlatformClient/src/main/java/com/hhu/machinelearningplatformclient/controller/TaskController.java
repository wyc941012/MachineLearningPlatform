package com.hhu.machinelearningplatformclient.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.hhu.machinelearningplatformclient.data.FieldInfo;
import com.hhu.machinelearningplatformclient.entity.Response;
import com.hhu.machinelearningplatformclient.entity.ResponseCode;
import com.hhu.machinelearningplatformclient.entity.TaskInit;
import com.hhu.machinelearningplatformclient.task.TaskExecution;
import com.hhu.machinelearningplatformclient.task.TaskType;

@Controller
@RequestMapping("/task")
public class TaskController {

	@Resource
	private TaskExecution taskExecution;
	
	//提交任务
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/submitTask", method=RequestMethod.POST)
	@ResponseBody
	public Response submitTask(HttpServletRequest request) {
		Response response=new Response();
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest) request;
		MultipartFile multipartFile=multipartRequest.getFile("file");
		int taskType=Integer.valueOf(multipartRequest.getParameter("taskType"));
		TaskInit taskInit=new TaskInit();
		if(taskType==TaskType.ESTIMATOR_TYPE.getValue()) {
			int algorithmId=Integer.valueOf(multipartRequest.getParameter("algorithmId"));
			taskInit.setAlgorithmId(algorithmId);
		}
		if(taskType==TaskType.TRANSFORMER_TYPE.getValue()) {
			int modelId=Integer.valueOf(multipartRequest.getParameter("modelId"));
			taskInit.setModelId(modelId);
		}
		String parameterMap=multipartRequest.getParameter("parameterMap");
		ObjectMapper objectMapper=new ObjectMapper();
		Map<String, String> parameterValue=null;
		List<FieldInfo> fieldInfo=null;
		String dataFileCol=multipartRequest.getParameter("dataFileCol");
		try {
			parameterValue=objectMapper.readValue(parameterMap, Map.class);
			fieldInfo=objectMapper.readValue(dataFileCol, List.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setResponseCode(ResponseCode.SUCCESS);
			response.setMessage("任务提交失败！");
			return response;
		}
		int sparkExecutorNum=Integer.valueOf(multipartRequest.getParameter("sparkExecutorNum"));
		String dataFileType=multipartRequest.getParameter("dataFileType");
		String delim=multipartRequest.getParameter("dataFileDelim");
		taskInit.setDataFileType(dataFileType);
		taskInit.setDelim(delim);
		taskInit.setFieldInfo(fieldInfo);
		taskInit.setMultipartFile(multipartFile);
		taskInit.setParameterValue(parameterValue);
		taskInit.setSparkExecutorNum(sparkExecutorNum);
		taskExecution.taskInit(taskInit);
		response.setResponseCode(ResponseCode.SUCCESS);
		response.setMessage("任务提交成功！");
		return response;
	}
	
}
