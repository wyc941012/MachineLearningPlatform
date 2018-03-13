package com.hhu.machinelearningplatformserver.proxy;

import java.lang.reflect.Method;
import java.util.Map;

import com.hhu.machinelearningplatformserver.algorithm.MLAlgorithmDesc;
import com.hhu.machinelearningplatformserver.algorithm.MLAlgorithmLoader;
import com.hhu.machinelearningplatformserver.algorithm.ParameterDesc;
import com.hhu.machinelearningplatformserver.task.SparkTaskAlgorithm;

/**
 * 算法代理
 *
 * @author hayes, @create 2017-12-12 16:16
 **/
public class MLAlgorithmProxy {

    /**
     * 算法实例
     */
    protected Object algo;

    protected Class<?> algoClazz;

    protected MLAlgorithmDesc desc;

    MLAlgorithmProxy(SparkTaskAlgorithm mlAlgo) throws Exception {
        desc = MLAlgorithmLoader.getMLAlgorithmDesc(mlAlgo.getId());
        algoClazz = Class.forName(desc.getClassName());
        algo = algoClazz.newInstance();

        for (Map.Entry<String, String> param : mlAlgo.getParameters().entrySet()) {
            //获得参数描述
            ParameterDesc paramDesc = desc.getParameterDescs().get(param.getKey());
            String setterMethodName = param.getKey().substring(0, 1).toUpperCase()
                    + param.getKey().substring(1);
            Method method = algoClazz.getMethod("set" + setterMethodName,
                    paramDesc.javaTypeClass());
            method.invoke(algo, paramDesc.valueOf(param.getValue()));
        }
    }
}
