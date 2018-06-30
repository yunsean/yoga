package com.yoga.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapConverter<T> {

	public static class MapItem<K, V> extends HashMap<K, V> {
		private static final long serialVersionUID = -9148459395437048765L;	
		public MapItem<K, V> set(K key, V value) {
			if (value == null)return this;
			super.put(key, value);
			return this;
		}
	}
	public interface Converter<T> {
		public void convert(T item, MapItem<String, Object> map);
	}
	
	private Converter<T> mObject2Map = null;
	public MapConverter(Converter<T> converter) {
		mObject2Map = converter;
	}
	public ArrayList<Map<String, Object>> build(Iterable<T> collection) {
		ArrayList<Map<String, Object>> maps = new ArrayList<>();
		if (collection == null)return maps;
		for (T item : collection) {
			MapItem<String, Object> map = new MapItem<>();
			mObject2Map.convert(item, map);
			maps.add(map);
		}
		return maps;
	}
	public MapItem<String, Object> build(T item) {
		if (item == null)return null;
		MapItem<String, Object> map = new MapItem<>();
		mObject2Map.convert(item, map);
		return map;
	}
}
