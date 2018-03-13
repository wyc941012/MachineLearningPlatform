package com.hhu.machinelearningplatformserver.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.hhu.machinelearningplatformserver.exception.ConfigInitException;

//读取并加载配置文件中的信息
public class ConfigUtils {

private static Map<String, String> map=new ConcurrentHashMap<String, String>();
	
	public static void readConfig() throws Exception {
		Properties properties=new Properties();
		try {
			//读取属性文件
			InputStream inputStream=new BufferedInputStream(new FileInputStream(ResourcePath.RESOURCE_DIR+"hdfs.properties"));
			//加载属性列表
			properties.load(inputStream);
			Iterator<String> iterator=properties.stringPropertyNames().iterator();
			if(!iterator.hasNext()) {
				System.out.println("未读取到配置信息！");
			}
			while(iterator.hasNext()) {
				String key=iterator.next();
				String value=properties.getProperty(key);
				map.put(key, value);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("读取配置信息失败！");
			throw e;
		}
	}
	
	//按照名称获取配置信息
	public static String getValueByName(String name) {
		String value=map.get(name);
		return value;
	}
	
	//初始化配置信息
	public static void initConfig() throws ConfigInitException, IOException {
		String HDFS_IP=getValueByName("hdfs.hostname");
		if(HDFS_IP==null) {
			throw new ConfigInitException("HDFS集群IP地址不能为空！");
		}
		String HDFS_PORT=getValueByName("hdfs.port");
		if(HDFS_PORT==null) {
			throw new ConfigInitException("HDFS集群端口号不能为空！");
		}
		String HDFS_USER=getValueByName("hdfs.user");
		if(HDFS_USER==null) {
			throw new ConfigInitException("HDFS集群用户名不能为空！");
		}
		String HDFS_URI=getValueByName("hdfs.uri");
		if(HDFS_URI==null) {
			throw new ConfigInitException("HDFS集群根目录不能为空！");
		}
		try {
			HDFSUtils.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ConfigInitException("HDFS初始化失败！");
		}
		
	}
	
	public static void addConfig(String key, String value) {
		map.put(key, value);
	}
	
	public static void deleteConfig(String key) {
		map.remove(key);
	}
	
}
