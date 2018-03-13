package com.hhu.machinelearningplatformserver.proxy;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 训练后的模型代理
 *
 * @author hayes, @create 2017-12-12 15:21
 **/
public class ModelProxy {

    /**
     * 模型实例
     */
    protected Object model;

    protected Class<?> modelClazz;

    public ModelProxy(Object model, Class<?> modelClazz) {
        this.model = model;
        this.modelClazz = modelClazz;
    }

    private ModelProxy() {
    }

    /**
     * TODO： 加载
     *
     * @param path
     * @param modelClazz
     * @return
     */
    public static ModelProxy load(String path, Class<?> modelClazz) {
        ModelProxy modelProxy = new ModelProxy();
        modelProxy.modelClazz = modelClazz;

        return modelProxy;
    }

    @SuppressWarnings("unchecked")
	public Dataset<Row> tranform(Dataset<Row> dataset) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = modelClazz.getMethod("save", Dataset.class);
        return (Dataset<Row>) method.invoke(model, dataset);
    }

    public void save(String path) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = modelClazz.getMethod("save", String.class);
        method.invoke(model,path);
    }
}
