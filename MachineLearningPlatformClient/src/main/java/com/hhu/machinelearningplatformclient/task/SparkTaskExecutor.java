package com.hhu.machinelearningplatformclient.task;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhu.machinelearningplatformclient.common.RedisUtils;
import com.hhu.machinelearningplatformclient.config.SystemConfig;

/**
 * spark task executor
 *
 * @author hayes, @create 2017-12-19 14:40
 **/
@Component
public class SparkTaskExecutor {

	@Resource
	private RedisUtils redisUtils;
    @Autowired
    private SystemConfig systemConfig;


    /**
     * 执行Spark任务  TODD: Process的管理
     *
     * @param taskInfo
     * @return
     * @throws IOException
     */
    public void execute(TaskInfo taskInfo) throws IOException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(systemConfig.getSparkK8sDir()));
        pb.command(buildCommand(taskInfo));
        Process p = pb.start();
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        if ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        redisUtils.put(taskInfo.getTaskId(), TaskState.RUNNING.getValue());
    }


    public List<String> buildCommand(TaskInfo taskInfo) {
        List<String> command = new ArrayList<>();
        command.add("./spark-submit");
        command.add("--deploy-mode");
        command.add("cluster");
        command.add("--master");
        command.add(systemConfig.getK8sMaster());
        command.add("--class");
        command.add("edu.hhu.stonk.spark.Submiter");
        command.add("--kubernetes-namespace");
        command.add(systemConfig.getK8sSparkNamespace());
        command.add("--conf");
        command.add("spark.executor.instances=" + taskInfo.getSparkExecutorNum());
        command.add("--conf");
        command.add("spark.app.name=" + taskInfo.getName());
        command.add("--conf");
        command.add("spark.kubernetes.driver.docker.image=" + systemConfig.getK8sSparkDriverDockerImage());
        command.add("--conf");
        command.add("spark.kubernetes.executor.docker.image=" + systemConfig.getK8sSparkExecutorDockerImage());
        command.add("--conf");
        command.add("spark.kubernetes.authenticate.driver.serviceAccountName=" + systemConfig.getK8sSparkServiceAccountName());
        command.add(systemConfig.getTaskJarPath());
        command.add(systemConfig.getHdfsMaster());
        command.add(String.valueOf(taskInfo.getTaskId()));
        command.add(String.valueOf(taskInfo.getUserId()));

        return command;
    }
}
