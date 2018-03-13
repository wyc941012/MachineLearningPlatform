package com.hhu.machinelearningplatformserver.common;

import java.io.IOException;
import java.util.PropertyResourceBundle;

public class JRedisPoolConfig {
	private static PropertyResourceBundle propertyResourceBundle;
	static {
		try {
			propertyResourceBundle=new PropertyResourceBundle(JRedisPoolConfig.class.getResourceAsStream("/redis.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static int EXPIRE=Integer.parseInt(propertyResourceBundle.getString("EXPIRE_TIME"));
	static int MAX_ACTIVE=Integer.parseInt(propertyResourceBundle.getString("maxActive"));
	static int MAX_IDLE=Integer.parseInt(propertyResourceBundle.getString("maxIdle"));
	static int MAX_WAIT=Integer.parseInt(propertyResourceBundle.getString("maxWait"));
	static int TIME_OUT=Integer.parseInt(propertyResourceBundle.getString("timeout"));
	static String REDIS_PASSWORD=propertyResourceBundle.getString("redisPWD");
	static String REDIS_IP=propertyResourceBundle.getString("redisIp");
	static int REDIS_PORT=Integer.parseInt(propertyResourceBundle.getString("redisPort"));
}
