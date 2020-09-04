package com.yoga.core.data;

import java.util.ArrayList;
import java.util.Map;

public class MapConverter<T> {

	public interface Converter<T> {
		public void convert(T item, ChainMap<String, Object> map);
	}
	
	private Converter<T> mObject2Map = null;
	public MapConverter(Converter<T> converter) {
		mObject2Map = converter;
	}
	public ArrayList<Map<String, Object>> build(Iterable<T> collection) {
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		if (collection == null)return maps;
		for (T item : collection) {
			ChainMap<String, Object> map = new ChainMap<>();
			mObject2Map.convert(item, map);
			maps.add(map);
		}
		return maps;
	}
	public ChainMap<String, Object> build(T item) {
		if (item == null)return null;
		ChainMap<String, Object> map = new ChainMap<>();
		mObject2Map.convert(item, map);
		return map;
	}
}
