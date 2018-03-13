package com.hhu.machinelearningplatformserver.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtils {
	
	private static JedisPool pool=null;
	private static JedisUtils jedisUtils;
	private final int expire =JRedisPoolConfig.EXPIRE;
	
	static {
		JedisPoolConfig config=new JedisPoolConfig();
		config.setMaxTotal(JRedisPoolConfig.MAX_ACTIVE);
		config.setMaxIdle(JRedisPoolConfig.MAX_IDLE);
		config.setMaxWaitMillis(JRedisPoolConfig.MAX_WAIT);
		//在获取连接的时候检查有效性
		config.setTestOnBorrow(false);
		//在return给pool时,是否提前进行validate操作
		config.setTestOnReturn(false);
		pool=new JedisPool(config,JRedisPoolConfig.REDIS_IP,JRedisPoolConfig.REDIS_PORT,JRedisPoolConfig.TIME_OUT);
	}
	
	public static JedisPool getPool() {
		return pool;
	}
	
	//从jedis连接池中获取jedis对象
	public static Jedis getJedis() {  
		return pool.getResource();
	}
	
	public static JedisUtils getInstance() {
		return jedisUtils;
	}
	
	//回收jedis
	public static void returnJedis(Jedis jedis) {
		pool.returnResource(jedis);
	} 
	
	//设置过期时间
	public void expire(String key,int seconds) {
		if(seconds<0) {
			return;
		}
		Jedis jedis=getJedis();
		jedis.expire(key, seconds);
		returnJedis(jedis);
	}
	
	//设置默认过期时间
	public void expire(String key) {
		expire(key, expire);
	}
	
	
	
}
