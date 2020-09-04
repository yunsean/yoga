package com.yoga.core.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yoga.core.utils.AssertUtil;
import com.yoga.core.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.util.SafeEncoder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisOperator{
	@Autowired
	private StringRedisTemplate template;
	
	private ValueOperations<String, String> getOps(){
		return template.opsForValue();
	}

	private ZSetOperations<String, String> getZSetOps() {
		return template.opsForZSet();
	}
	
	private HashOperations<String, String, String> getOpsForHash(){
		return template.opsForHash();
	}

	public StringRedisTemplate getTemplate() {
		return template;
	}

	public <HK, HV> void hashSet(String key, Map<HK, HV> values){
		AssertUtil.notNull(values);
		Map<String, String> waitPut = new HashMap<>();
		for (Map.Entry<HK, HV> v : values.entrySet()){
			waitPut.put(JsonUtil.toJson(v.getKey()), JsonUtil.toJson(v.getValue()));
		}
		getOpsForHash().putAll(key, waitPut);
	}
	
	public <HK, HV> Map<HK, HV> hashEntries(String key, Class<?> hkClass, Class<?> hvClass){
		Map<String, String> saved = getOpsForHash().entries(key);
		if (null == saved) return null;
		Map<HK, HV> target = new HashMap<HK, HV>();
		for(Map.Entry<String, String> v : saved.entrySet()){
			target.put(JsonUtil.toObject(v.getKey(), hkClass), JsonUtil.toObject(v.getValue(), hvClass));
		}
		return target;
	}
	
	public <HK, HV> HV hashGet(String key, HK hashKey, Class<?> hvClass){
		
		String saved = getOpsForHash().get(key, JsonUtil.toJson(hashKey));
		if(null == saved){
			return null;
		}
		return JsonUtil.toObject(saved, hvClass);
	}
	
	public <HK, HV> List<HV> hashGets(String key, Collection<HK> hashKeys, Class<?> hvClass){
		List<String> hashKeyStrs = convertToHK(hashKeys);
		List<String> saved = getOpsForHash().multiGet(key, hashKeyStrs);
		List<HV> target = new ArrayList<>();
		for(String v : saved){
			target.add(JsonUtil.toObject(v, hvClass));
		}
		return target;
	}
	
	private static <HK> List<String> convertToHK(Collection<HK> hashKeys){
		List<String> hashKeyStrs = new ArrayList<String>();
		for(HK hashKey : hashKeys){
			hashKeyStrs.add(JsonUtil.toJson(hashKey));
		}
		return hashKeyStrs;
	}
	
	public <HK> void hashRemove(String key, Collection<HK> hashKeys){
		List<String> hashKeyStrs = convertToHK(hashKeys);
		getOpsForHash().delete(key, hashKeyStrs.toArray());
	}

	private ListOperations<String, String> getListOps(){
		return template.opsForList();
	}

	public long getExpire(String key) {
		return template.getExpire(key);
	}
	
	public void setExpire(String key, long expire, TimeUnit unit) {
		template.expire(key, expire, unit);
	}

	public void zset(String key, String value, long score) {
		this.getZSetOps().add(key, value, score);
	}
	public Double zscore(String key, String value) {
		return this.getZSetOps().score(key, value);
	}
	public void zadd(String key, String value, long score) {
		this.getZSetOps().incrementScore(key, value, score);
	}
	public void zrem(String key, String value) {
		this.getZSetOps().remove(key, value);
	}
	public Set<String> zrange(String key, int count) {
		return this.getZSetOps().range(key, 0, count);
	}

	public void set(String key, String value) {
		this.getOps().set(key, value);
	}
	public boolean setNX(String key, String value) {
		final byte[] rawKey = key.getBytes();
		final byte[] rawValue = value.getBytes();
		return template.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				if (redisConnection.setNX(rawKey, rawValue)) return true;
				return false;
			}
		});
	}
	public boolean setNX(String key, Object value, long expireSeconds) {
		final byte[] rawKey = key.getBytes();
		final String json = JSON.toJSONString(value);
		final byte[] rawValue = json.getBytes();
		return template.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				Object obj = redisConnection.execute("set", rawKey, rawValue, SafeEncoder.encode("NX"), SafeEncoder.encode("EX"), Protocol.toByteArray(expireSeconds));
				return obj != null;
			}
		});
	}
	public String getSet(final String key, final String value) {
		return template.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				StringRedisSerializer serializer = new StringRedisSerializer();
				byte[] ret = connection.getSet(serializer.serialize(key), serializer.serialize(value));
				connection.close();
				return serializer.deserialize(ret);
			}
		});
	}

	public void set(String key, String value, long expireSeconds) {
		this.getOps().set(key, value, expireSeconds, TimeUnit.SECONDS);
	}

	public void set(String key, Object value) {
		String json = JSON.toJSONString(value);
		this.getOps().set(key, json);
	}
	
	public void set(String key, Object value, long expireSeconds) {
		String json = JSON.toJSONString(value);
		this.getOps().set(key, json, expireSeconds, TimeUnit.SECONDS);
	}

	public void set(String key, String value, long expire, TimeUnit unit) {
		this.getOps().set(key, value, expire, unit);
	}
	
	public String get(String key) {
		return this.getOps().get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<?> clazz) {
		String json = get(key);
		return (T)JSON.parseObject(json, clazz);
	}

	public <T> T get(String key, TypeReference type) {
		String json = get(key);
		return (T)JSON.parseObject(json, type);
	}

	public void remove(String key) {
		template.delete(key);
	}
	
	public void publish(String channel, String key) {
		this.getListOps().leftPush(channel, key);
	}

	public Set<String> keys(String fuzzyKey) {
		return template.keys(fuzzyKey);
	}

	public Collection<String> mget(Set<String> keys) {
		return this.getOps().multiGet(keys);
	}

	public void removePattern(String keyPattern) {
		Set<String> keys = template.keys(keyPattern);
		if (keys != null && keys.size() > 0) {
			template.delete(keys);
		}
	}

}
