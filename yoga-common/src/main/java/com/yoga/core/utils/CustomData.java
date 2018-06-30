package com.yoga.core.utils;

import java.util.HashMap;
import java.util.Map;

public class CustomData{
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public CustomData put(String key, Object value){
		map.put(key, value);
		return this;
	}
	
	public Map<String, Object> getMap(){
		return map;
	}
	
}
