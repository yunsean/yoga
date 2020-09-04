package com.yoga.core.utils;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * sql构造器（防止sql注入）
 * @author Skysea
 *
 */
public class SqlBuilder {
	private BuildResult result = new BuildResult();
	public SqlBuilder put(String sqlPart){
		result.getSqlBuilder().append(sqlPart + " ");
		return this;
	}
	public SqlBuilder put(String sqlPart, Object param){
		return put(sqlPart, param, Types.NULL);
	}

	public SqlBuilder put(String sqlPart, Object param, int type){
		if(null == param){
			return this;
		}
		result.getSqlBuilder().append(sqlPart + " ");
		result.getParamBuilder().add(param);
		result.getTypeBuilder().add(type);
		return this;
	}

	public SqlBuilder put(String sqlPart, Object[] params, int [] types){
		if(null == params || params.length == 0){
			return this;
		}
		result.getSqlBuilder().append(sqlPart + " ");
		result.getParamBuilder().addAll(Arrays.asList(params));
		for(int type : types){
			result.getTypeBuilder().add(type);
		}
		return this;
	}

	public BuildResult getResult(){
		return result;
	}

	public static class BuildResult{
		private StringBuilder sqlBuilder;
		private List<Object> paramBuilder;
		private List<Integer> typeBuilder;
		private BuildResult(){
			this.sqlBuilder = new StringBuilder();
			this.paramBuilder = new ArrayList<>();
			this.typeBuilder = new ArrayList<>();
		}
		private StringBuilder getSqlBuilder() {
			return sqlBuilder;
		}
		private List<Object> getParamBuilder() {
			return paramBuilder;
		}
		private List<Integer> getTypeBuilder() {
			return typeBuilder;
		}
		public String getSql(){
			return sqlBuilder.toString();
		}
		public Object[] getParams(){
			return paramBuilder.toArray();
		}
		public int [] getTypes(){
			if(typeBuilder.size() == 0){
				return new int[0];
			}
			int [] types = new int[typeBuilder.size()];
			int index = 0;
			for(Integer type : typeBuilder){
				types[index ++] = type; 
			}
			return types;
		}
	}
}
