package com.hhu.machinelearningplatformserver.proxy;

import org.apache.spark.ml.Model;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import com.hhu.machinelearningplatformserver.algorithm.MLAlgorithmDesc;
import com.hhu.machinelearningplatformserver.algorithm.MLAlgorithmLoader;
import com.hhu.machinelearningplatformserver.common.ConfigUtils;
import com.hhu.machinelearningplatformserver.model.AlgorithmModel;
import java.lang.reflect.Method;

/**
 * Transformers Proxy
 *
 * @author hayes, @create 2017-12-12 15:20
 **/
public class TransformerProxy {
	
	private Object modelObject;
	
	@SuppressWarnings("rawtypes")
	private Model modelProxy;
	
    @SuppressWarnings("rawtypes")
	public TransformerProxy(AlgorithmModel model) throws Exception {
        int algorithmId=model.getAlgorithmId();
        MLAlgorithmDesc algorithmDesc=MLAlgorithmLoader.getMLAlgorithmDesc(algorithmId);
        Class<?> clazz=Class.forName(algorithmDesc.getClassName());
        Method method=clazz.getMethod("load", String.class);
        modelObject=clazz.newInstance();
        String hostname=ConfigUtils.getValueByName("hdfs.hostname");
        String port=ConfigUtils.getValueByName("hdfs.port");
        String uri=ConfigUtils.getValueByName("hdfs.uri");
        modelProxy=(Model) method.invoke(modelObject, "hdfs://"+hostname+":"+port+uri+"model/"+model.getId()+"/");
    }

    @SuppressWarnings("unchecked")
	public Dataset<Row> transform(Dataset<Row> dataset) throws Exception {
        Method method = modelProxy.getClass().getMethod("transform", Dataset.class);
        return (Dataset<Row>) method.invoke(modelProxy, dataset);
    }
}
