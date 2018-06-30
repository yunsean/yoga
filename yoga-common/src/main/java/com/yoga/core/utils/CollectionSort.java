package com.yoga.core.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CollectionSort {
	public static List<Map.Entry<String, String>> sortMap(final Map<String, String> map) {
        final List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        // 重写集合的排序方法：按字母顺序
        Collections.sort(infos, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(final Entry<String, String> o1, final Entry<String, String> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey()));
            }
        });
        
        return infos;
    }
}
