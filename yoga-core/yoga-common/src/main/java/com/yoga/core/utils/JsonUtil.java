/**
 * Alipay.com Inc. Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yoga.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 券核心Json工具类
 * 
 * @author jian.gj
 * @version $Id: VccJsonUtil.java, v 0.1 2015年2月7日 下午5:38:09 jian.gj Exp $
 */
public class JsonUtil {

    /** 序列化配置 */
    private static final SerializerFeature[] SERIALIZER_FEATURES;

    /** 序列化配置 */
    private static final SerializerFeature[] SERIALIZER_FEATURES_WITH_TYPE;

    static {
        // 增加money解析器
        //ParserConfig.getGlobalInstance().getDerializers().put(Money.class, new MoneyJsonConvertor());
        //SerializeConfig.getGlobalInstance().put(Money.class, new MoneyJsonConvertor());

        //序列化配置 1.日期格式使用默认格式
        SERIALIZER_FEATURES = new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat };

        // 日期默认格式,保存类名
        SERIALIZER_FEATURES_WITH_TYPE = new SerializerFeature[] {
                SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteClassName };
    }

    /**
     * 将对象转换为Json字符串
     * 
     * @param obj 待转换对象
     * @return Json字符串
     */
    public static String toJson(final Object obj) {
        return JSON.toJSONString(obj, SERIALIZER_FEATURES);
    }

    /**
     * 将对象转换为Json字符串
     * <p>带过滤字段功能
     * 
     * @param obj   待转换对象
     * @param keys  属性值
     * @return Json字符串
     */
    public static String toJson(final Object obj, final List<String> keys) {
        SerializeFilter filter = new PropertyFilter() {
            /**
             * @see PropertyFilter#apply(Object, String, Object)
             */
            @Override
            public boolean apply(final Object source, final String name, final Object value) {
                return keys.contains(name);
            }
        };
        return JSON.toJSONString(obj, filter, SERIALIZER_FEATURES);
    }

    /**
     * 将对象转换为Json字符串，字符串带上具体类型
     * <p>
     * 目前仅用于内部带泛型或Object类型数据
     *
     * @param obj 待转换对象
     * @return Json字符串
     */
    public static String toJsonWithType(final Object obj) {
        return JSON.toJSONString(obj, SERIALIZER_FEATURES_WITH_TYPE);
    }

    /**
     * 将json字符串转换为对象
     * 
     * @param clz 目标类型
     * @param json json字符串
     * @param <T> 目标类型
     * @return 目标对象
     */
    public static <T> T fromJson(final Class<T> clz, final String json) {
        if (StringUtil.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, clz);
    }

    /**
     * 将json字符串转换为对象
     * 
     * @param json json字符串
     * @return 目标对象
     */
    public static Map<String, Object> fromJsonMap(final String json) {
        if (StringUtil.isBlank(json)) {
            return new HashMap<String, Object>();
        }
        return JSON.parseObject(json);
    }

    /**
     * 将json字符串转换为对象列表
     * 
     * @param clz 目标类型
     * @param json json字符串
     * @param <T> 目标类型
     * @return 目标对象列表
     */
    public static <T> List<T> fromJsonArray(final Class<T> clz, final String json) {
        if (StringUtil.isBlank(json)) {
            return new ArrayList<T>();
        }
        return DefaultUtil.defaultEditableList(JSON.parseArray(json, clz));
    }

    /**
     * 获取Json中的值
     * 
     * @param json json
     * @param key  键
     * @return 值
     */
    public static String getJsonValue(final String json, final String key) {
        return (String) DefaultUtil.defaultEditableMap(fromJsonMap(json)).get(key);
    }

    /**
     * 剔除map里边的固定key
     * 
     * @param map  json格式的map
     * @param keys  key列表
     * @return  新的map
     */
    public static Map<String, Object> removeMapValueByKeys(final Map<String, Object> map,
                                                            final String... keys) {

        for (String key : keys) {
            if (map.containsKey(key)) {
                map.remove(key);
            }
        }
        return map;
    }

    /**
     * 往json里边放map
     * 
     * @param json  json字符串
     * @param map   map
     * @return  新的json字符串
     */
    public static String putMapToJson(final String json, final Map<String, Object> map) {

        Map<String, Object> originalMap = fromJsonMap(json);
        originalMap.putAll(map);
        return toJson(originalMap);
    }

    /**
     * 往map里边放map
     * 
     * @param originalMap  原始map
     * @param map   map
     * @return  新的json字符串
     */
    public static Map<String, Object> putMapToMap(final Map<String, Object> originalMap,
                                     final Map<String, Object> map) {

        originalMap.putAll(map);
        return originalMap;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toObject(String json, Class<?> clazz){
        return (T)JSON.parseObject(json, clazz);
    }

    public static <T> List<T> toObjects(String json, Class<T> clazz){
        return JSON.parseArray(json, clazz);
    }
}
