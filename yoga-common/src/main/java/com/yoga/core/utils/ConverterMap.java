package com.yoga.core.utils;

import java.util.ArrayList;
import java.util.Map;

public class ConverterMap<T> {

	public interface Converter<T> {
		public T convert(Map<String, Object> map);
	}
	private Converter<T> mObject2Map = null;
	public ConverterMap(Converter<T> converter) {
		mObject2Map = converter;
	}
	public ArrayList<T> build(Iterable<Map<String, Object>> collection) {
		if (collection == null)return null;
		ArrayList<T> maps = new ArrayList<>();
		for (Map<String, Object> item : collection) {
			T o = mObject2Map.convert(item);
			maps.add(o);
		}
		return maps;
	}
	public T build(Map<String, Object> item) {
		if (item == null)return null;
		return mObject2Map.convert(item);
	}
}
