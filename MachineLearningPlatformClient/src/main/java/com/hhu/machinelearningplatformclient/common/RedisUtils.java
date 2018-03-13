package com.hhu.machinelearningplatformclient.common;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	public Object get(Object key) {
		String keyRedis=key.toString();
		Object object=null;
		object=redisTemplate.execute(new RedisCallback<Object>() {

			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				// TODO Auto-generated method stub
				byte[] key=keyRedis.getBytes();
				byte[] value=connection.get(key);
				if(value==null) {
					return null;
				}
				return ByteObjectUtil.ByteToObject(value);
			}
			
		});
		if(object==null) {
			return null;
		}
		else {
			return object;
		}
	}
	
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		String keyRedis=key.toString();
		long liveTime = 86400;
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				// TODO Auto-generated method stub
				byte[] key=keyRedis.getBytes();
				byte[] value1=ByteObjectUtil.ObjectToByte(value);
				connection.set(key, value1);
				if(liveTime>0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}
	
	public void clear() {
		// TODO Auto-generated method stub 
		redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				// TODO Auto-generated method stub
				connection.flushDb();
				return "ok";
			}
		});
	}
	
}
