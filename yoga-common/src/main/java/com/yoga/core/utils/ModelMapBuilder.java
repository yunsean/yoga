package com.yoga.core.utils;

import com.yoga.core.data.CommonPage;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;
/**
 * Controller web返回值封装
 * @author Skysea
 *
 */
public class ModelMapBuilder {
	
	private ModelMap model;
	private Map<String, Object> params = new HashMap<String, Object>();
	public ModelMapBuilder(ModelMap model){
		this.model = model;
	}
	public ModelMapBuilder put(String key, Object value){
		model.put(key, value);
		return this;
	}
	public ModelMapBuilder setPage(CommonPage page){
		model.put("page", page);
		return this;
	}
	public ModelMapBuilder setSearchParam(String key, Object value){
		params.put(key, value);
		return this;
	}
	public void build(){
		model.put("param", params);
	}
}
