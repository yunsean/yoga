package com.yoga.core.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * json工具类
 * @author Skysea
 * 
 */
public class JsonOperator {
	
	public static String toJson(Object src){
		return JSON.toJSONString(src);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T toObject(String json, Class<?> clazz){
		return (T)JSON.parseObject(json, clazz);
	}
	
	public static <T> List<T> toObjects(String json, Class<T> clazz){
		return JSON.parseArray(json, clazz);
	}
}
