package com.hhu.machinelearningplatformclient.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config
 *
 * @author hayes, @create 2017-12-19 13:00
 **/
@Component
public class SystemConfig {

    private static final String CONF_FILE_PATH = "/kubernetes.properties";

    private String k8sMaster;

    private String k8sSparkNamespace;

    private String k8sSparkServiceAccountName;

    private String k8sSparkDriverDockerImage;

    private String k8sSparkExecutorDockerImage;

    private String hdfsMaster;

    private String taskJarPath;

    private String sparkK8sDir;


    @PostConstruct
    public void load() throws IOException {
        InputStream in = this.getClass().getResourceAsStream(CONF_FILE_PATH);
        Properties pro = new Properties();
        pro.load(in);
        k8sMaster = pro.getProperty("k8s.master", "10.196.83.65:6443");
        k8sSparkNamespace = pro.getProperty("k8s.spark.namespace", "spark-cluster");
        k8sSparkServiceAccountName = pro.getProperty("k8s.spark.serviceAccountName", "spark-admin");
        k8sSparkDriverDockerImage = pro.getProperty("k8s.spark.driver.docker.image");
        k8sSparkExecutorDockerImage = pro.getProperty("k8s.spark.executor.docker.image");
        hdfsMaster = pro.getProperty("hdfs.master");
        taskJarPath = pro.getProperty("task.jar.path");
        sparkK8sDir = pro.getProperty("spark-k8s.dir");
    }


    public String getK8sMaster() {
        return k8sMaster;
    }

    public String getK8sSparkNamespace() {
        return k8sSparkNamespace;
    }

    public String getK8sSparkServiceAccountName() {
        return k8sSparkServiceAccountName;
    }

    public String getK8sSparkDriverDockerImage() {
        return k8sSparkDriverDockerImage;
    }

    public String getK8sSparkExecutorDockerImage() {
        return k8sSparkExecutorDockerImage;
    }

    public String getHdfsMaster() {
        return hdfsMaster;
    }

    public String getTaskJarPath() {
        return taskJarPath;
    }

    public String getSparkK8sDir() {
        return sparkK8sDir;
    }

    public void setSparkK8sDir(String sparkK8sDir) {
        this.sparkK8sDir = sparkK8sDir;
    }
}
