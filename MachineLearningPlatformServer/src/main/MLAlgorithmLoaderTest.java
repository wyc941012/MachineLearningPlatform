package edu.hhu.stonk.spark.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hhu.stonk.spark.mllib.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.feature.LabeledPoint;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * MLAlgorithmLoader测试类
 *
 * @author hayes, @create 2017-12-14 19:35
 **/
public class MLAlgorithmLoaderTest {
    
    public static void main(String[] args) {
    	SparkConf conf=new SparkConf().setAppName(appName).setMaster(master);
		JavaSparkContext sc=new JavaSparkContext(conf);
		LabeledPoint point=new LabeledPoint(1.0, Vectors.dense(2.0, 3.0, 3.0));
		Vector v=Vectors.dense({1,1,2});
		
	}
}
