package com.commons.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fengjc on 14/12/16
 */
public class RedisClient extends JedisPool {
	
	public RedisClient(GenericObjectPoolConfig poolConfig, String host, int port) {
		super(poolConfig, host, port);
	}

	public Set<String> keys(final String pattern) {
		return new Executor<Set<String>>(this) {
			@Override
			Set<String> execute() {
				return jedis.keys("*" + pattern + "*");
			}
		}.getResult();
	}

    public Long hdel(final String key, final String[] fields) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.hdel(key, fields);
			}
		}.getResult();
	}

    public Long hdel(final String key) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.hdel(key);
			}
		}.getResult();
	}

	public Long del(final String key) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.del(key);
			}
		}.getResult();
	}

	public Long del(final String... keys) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.del(keys);
			}
		}.getResult();
	}

	public String hget(final String key, final String field) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				return jedis.hget(key, field);
			}
		}.getResult();
	}

	public Map<String, String> hgetAll(final String key) {
		return new Executor<Map<String, String>>(this) {
			@Override
			Map<String, String> execute() {
				return jedis.hgetAll(key);
			}
		}.getResult();
	}

	public String hmset(final String key, final Map<String, String> hash) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				return jedis.hmset(key, hash);
			}
		}.getResult();
	}

	public String hmset(final String key, final Map<String, String> hash, long expireMilli) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				Pipeline pipeline = jedis.pipelined();
				pipeline.hmset(key, hash);
				pipeline.pexpire(key, expireMilli);
				pipeline.sync();
				return key;
			}
		}.getResult();
	}

	public String set(final String key, final String value) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				String rtn = jedis.set(key, value);
				return rtn;
			}
		}.getResult();
	}

	public String set(final String key, final String value, int expireSeconds) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				Pipeline pipeline = jedis.pipelined();
				pipeline.set(key, value);
				pipeline.expire(key, expireSeconds);
				pipeline.sync();
				return value;
			}
		}.getResult();
	}

	public Set<String> set(final Map<String, String> keyValues, long expireMilli) {
		return new Executor<Set<String>>(this) {
			@Override
			Set<String> execute() {
				Pipeline pipeline = jedis.pipelined();
				Set<String> result = new HashSet<>();
				for (Map.Entry<String, String> entry : keyValues.entrySet()) {
					pipeline.set(entry.getKey(), entry.getValue());
					pipeline.pexpire(entry.getKey(), expireMilli);
					result.add(entry.getKey());
				}
				pipeline.sync();
				return result;
			}
		}.getResult();
	}

	public Long hset(final String key, final String field, final String value) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.hset(key, field, value);
			}
		}.getResult();
	}

	public String get(final String key) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				return jedis.get(key);
			}
		}.getResult();
	}

	/**
	 * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, int start, int end) {
		return new Executor<List<String>>(this) {
			@Override
			List<String> execute() {
				return jedis.lrange(key, start, end);
			}
		}.getResult();
	}

	public Long rpush(String key, String... values) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.rpush(key, values);
			}
		}.getResult();
	}

	public Long lpush(String key, String... values) {
		final String[] reValues;
		if(values.length == 0) {
			reValues = new String[]{""};
		} else {
			reValues = values;
		}
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				Pipeline pipeline = jedis.pipelined();
				long l = jedis.lpush(key, reValues);
				pipeline.sync();
				return l;
			}
		}.getResult();
	}

	public Long lpush(String key, int seconds, String... values) {
		//增加判断values是否为空
		final String[] reValues;
		if(values.length == 0) {
			reValues = new String[]{""};
		} else {
			reValues = values;
		}
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				Pipeline pipeline = jedis.pipelined();
				long l = jedis.lpush(key, reValues);
				jedis.expire(key, seconds);
				pipeline.sync();
				return l;
			}
		}.getResult();
	}

	public String ltrim(String key, int start, int end) {
		return new Executor<String>(this) {
			@Override
			String execute() {
				return jedis.ltrim(key, start, end);
			}
		}.getResult();
	}

	public Long lrem(String key, int count, String value) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.lrem(key, count, value);
			}
		}.getResult();
	}

	public void rpop(String key, int len) {
		if (key == null)
			return;
		new Executor<Void>(this) {
			@Override
			Void execute() {
				Pipeline pipeline = jedis.pipelined();
				for (int i = 0; i < len; i++) {
					jedis.rpop(key);
				}
				pipeline.sync();
				return null;
			}
		}.getResult();
	}

	public Long setnx(String key, String value) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.setnx(key, value);
			}
		}.getResult();
	}

	public Long expire(String key, int seconds) {
		return new Executor<Long>(this) {
			@Override
			Long execute() {
				return jedis.expire(key, seconds);
			}
		}.getResult();
	}

	private abstract class Executor<T> {

		public Jedis jedis;
		private JedisPool this0;

		public Executor(JedisPool this0) {
			this.this0 = this0;
			this.jedis = this0.getResource();
		}

		abstract T execute();

		T getResult() {
			T result = null;
			try {
				result = execute();
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				if (jedis != null) {
					this0.returnResource(jedis);
				}
			}
			return result;
		}
	}

}
