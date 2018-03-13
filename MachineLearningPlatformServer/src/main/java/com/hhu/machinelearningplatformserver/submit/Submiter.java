package com.hhu.machinelearningplatformserver.submit;

import java.util.Random;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.codehaus.jackson.map.ObjectMapper;
import com.hhu.machinelearningplatformserver.common.HBaseUtil;
import com.hhu.machinelearningplatformserver.common.JedisUtils;
import com.hhu.machinelearningplatformserver.common.RandomUtil;
import com.hhu.machinelearningplatformserver.data.PersistDataset;
import com.hhu.machinelearningplatformserver.data.SparkDataFileConverter;
import com.hhu.machinelearningplatformserver.model.AlgorithmModel;
import com.hhu.machinelearningplatformserver.proxy.EstimatorProxy;
import com.hhu.machinelearningplatformserver.proxy.ModelProxy;
import com.hhu.machinelearningplatformserver.proxy.TransformerProxy;
import com.hhu.machinelearningplatformserver.task.TaskInfo;
import com.hhu.machinelearningplatformserver.task.TaskState;
import com.hhu.machinelearningplatformserver.task.TaskType;

import redis.clients.jedis.Jedis;

//提交Spark任务
public class Submiter {

	private static boolean localMode=false;
	
	private static TaskInfo taskInfo;
	
	private static Jedis jedis=JedisUtils.getJedis();
	
	private static int userId;
	
	/**
     * 该用户该任务的hdfs文件前缀
     */
    private static String hdfsFilePrefix;
	
	private static JavaSparkContext buildJavaSparkContext() {
      JavaSparkContext context = null;
      //本地模式
      if (localMode) {
          SparkConf conf = new SparkConf().setAppName(taskInfo.getName()).setMaster("local[3]");
          context = new JavaSparkContext(conf);
      } else {
          //JavaSparkContext初始化
          SparkSession sparkSession = SparkSession
                  .builder()
                  .appName(taskInfo.getName())
                  .getOrCreate();
          context = new JavaSparkContext(sparkSession.sparkContext());
      }      
      return context;
    }
	
	private static void loadArgs(String[] args) throws Exception {
       //配置
       long taskId = Long.valueOf(args[0]);
       userId=Integer.valueOf(args[1]);
       HBaseUtil hBaseUtil=HBaseUtil.getInstance();
       hBaseUtil.connection();
       String jsonStr=hBaseUtil.getValue("task", String.valueOf(taskId), "info", "taskInfo");
       ObjectMapper objectMapper=new ObjectMapper();
       taskInfo=objectMapper.readValue(jsonStr, TaskInfo.class);
       jedis.set(Bytes.toBytes(taskId), Bytes.toBytes(TaskState.RUNNING.getValue()));
       hdfsFilePrefix = new StringBuilder()  		   
               .append("/machinelearningplatform/task/")
               .append(taskId).append("/")
               .toString();
    }
	
	private static void excuteEstimator(TaskInfo taskInfo,
        Dataset<Row> dataset) throws Exception {
		EstimatorProxy estimatorProxy = new EstimatorProxy(taskInfo.getSparkTaskAlgorithm());
		ModelProxy modelProxy = estimatorProxy.fit(dataset);
		//生成模型
		int modelId=new Random().nextInt();
		AlgorithmModel model=new AlgorithmModel();
		model.setId(modelId);
		model.setName("模型-"+modelId);
		model.setUserId(userId);
		model.setCreateTime(System.currentTimeMillis());
		model.setAlgorithmId(taskInfo.getSparkTaskAlgorithm().getId());
		model.setPath("model/"+modelId+"/");
		//将模型信息写入到HBase
		HBaseUtil hBaseUtil=new HBaseUtil();
		hBaseUtil.connection();
		ObjectMapper objectMapper=new ObjectMapper();
		String jsonStr=objectMapper.writeValueAsString(model);
		hBaseUtil.putData("model", String.valueOf(modelId), "info", "modelInfo", jsonStr);
		hBaseUtil.close();
		modelProxy.save("/machinelearningplatform/model/");
	}

	private static void excuteTransformer(TaskInfo taskInfo,
              Dataset<Row> dataset) throws Exception {
		int modelId=taskInfo.getModelId();
		HBaseUtil hBaseUtil=new HBaseUtil();
		hBaseUtil.connection();
		String jsonStr=hBaseUtil.getValue("model", String.valueOf(modelId), "info", "modelInfo");
		ObjectMapper objectMapper=new ObjectMapper();
		AlgorithmModel model=objectMapper.readValue(jsonStr, AlgorithmModel.class);
		hBaseUtil.close();
		TransformerProxy transformerProxy = new TransformerProxy(model);
		Dataset<Row> transformedDataset = transformerProxy.transform(dataset);
		PersistDataset.persist(transformedDataset, hdfsFilePrefix + "out-" + RandomUtil.getRandomString(5));
	}
	
	public static void main(String[] args) {
		//加载配置
		try {
			loadArgs(args);
			JavaSparkContext context = buildJavaSparkContext();
			Dataset<Row> dataset=SparkDataFileConverter.extractDataFrame(taskInfo, context);
			TaskType taskType=taskInfo.getTaskType();
			if(taskType==TaskType.ESTIMATOR_TYPE) {
				excuteEstimator(taskInfo, dataset);
			}
			if(taskType==TaskType.TRANSFORMER_TYPE) {
				excuteTransformer(taskInfo, dataset);
			}
		    jedis.set(Bytes.toBytes(taskInfo.getTaskId()), Bytes.toBytes(TaskState.SUCCESS.getValue()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
		    jedis.set(Bytes.toBytes(taskInfo.getTaskId()), Bytes.toBytes(TaskState.FAIL.getValue()));
		}
	}
	
}
