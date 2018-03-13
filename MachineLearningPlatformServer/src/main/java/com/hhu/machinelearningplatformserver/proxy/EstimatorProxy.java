package com.hhu.machinelearningplatformserver.proxy;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import com.hhu.machinelearningplatformserver.task.SparkTaskAlgorithm;

import java.lang.reflect.Method;

/**
 * Estimator Proxy
 *
 * @author hayes, @create 2017-12-12 16:18
 **/
public class EstimatorProxy extends MLAlgorithmProxy {

    public EstimatorProxy(SparkTaskAlgorithm mlAlgo) throws Exception {
        super(mlAlgo);
    }

    public ModelProxy fit(Dataset<Row> dataset) throws Exception {
        Method method = algoClazz.getMethod("fit");
        Class<?> modelClass = Class.forName(this.desc.getClassName() + "Model");
        return new ModelProxy(method.invoke(algo, dataset), modelClass);
    }
}
