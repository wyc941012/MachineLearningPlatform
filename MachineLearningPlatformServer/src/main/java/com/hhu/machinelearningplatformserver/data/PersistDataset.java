package com.hhu.machinelearningplatformserver.data;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Persist dataset
 *
 * @author hayes, @create 2017-12-22 18:08
 **/
public class PersistDataset {
    public static void persist(Dataset<Row> dataset, String path) {
        dataset.javaRDD().saveAsTextFile(path);
//        StructType schema = dataset.schema();
//        StructField[] fields = schema.fields();
//        dataset.foreach((row) -> {
//
//        });
    }
}
