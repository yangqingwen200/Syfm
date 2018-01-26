package com.commons.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class CacheRedisClient extends com.commons.redis.RedisClient {

	public CacheRedisClient(GenericObjectPoolConfig poolConfig, String host, int port) {
		super(poolConfig, host, port);
	}
	
}
