package com.hhu.machinelearningplatformclient.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * task manager
 *
 * @author hayes, @create 2017-12-19 13:25
 **/
@Component
public class TaskManager {

    @Autowired
    SparkTaskExecutor sparkTaskExecutor;

    public void execute(TaskInfo taskInfo) throws IOException {
        sparkTaskExecutor.execute(taskInfo);
    }

}
