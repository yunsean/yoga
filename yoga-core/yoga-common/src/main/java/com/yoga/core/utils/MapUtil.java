/**
 * Alipay.com Inc.
 * Copyright(c) 2004-2017 All Rights Reserved.
 */
package com.yoga.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author raoqu
 * @version $Id: MapUtil.java v0.1 2017/10/7 下午7:26 raoqu Exp$
 */
public class MapUtil {

    public static Map<String, Object> newMap(String key, Object val) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, val);
        return map;
    }

    public static Map<String, Object> newMap(Object... keyVal) {
        Map<String, Object> map = new HashMap<>();
        for(int i=0;i<keyVal.length;i++){
            map.put(keyVal[i].toString(), keyVal[i+1]);
            i++;
        }
        return map;
    }

    /**
     * 合并
     * @param src
     * @param dest
     * @return
     */
    public static Map<String, Object> merge(Map<String, Object> src, Map<String, Object> dest) {
        Map<String, Object> map = new HashMap<>();
        map.putAll(src);
        Set<String> keyset = dest.keySet();
        for (String key : keyset) {
            map.put(key, dest.get(key));
        }
        return map;
    }

    public static double getDouble(Map<String, Object> extInfo, String start_price) {
        //TODO:
        return 0.0;
    }

}
