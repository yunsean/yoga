/**
 * Alipay.com Inc. Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.yoga.core.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 参数默认值工具类
 *
 * @author wb-zhoujian.zj
 * @version $Id: DefaultUtil.java, v 0.1 2014年10月17日 上午9:28:14 wb-zhoujian.zj Exp $
 */
public class DefaultUtil {
    /** 空列表  */
    public static final List<?> EMPTY_LIST = Collections.EMPTY_LIST;

    /** 空字符串  */
    public static final String EMPTY_STRING = StringUtil.EMPTY_STRING;

    /**
     * 获取默认值, 如果为空这返回空列表
     *
     * @param value 当前值 
     * @param <T> 列表类型
     * @return 参数值
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> defaultList(final List<T> value) {
        return defaultValue(value, (List<T>) EMPTY_LIST);
    }

    /**
     * 获取默认值, 如果为空这返回空列表(可编辑)
     *
     * @param value 当前值 
     * @param <T> 列表类型
     * @return 参数值
     */
    public static <T> List<T> defaultEditableList(final List<T> value) {
        if (null == value) {
            return new ArrayList<T>();
        }
        return value;
    }

    /**
     * 获取默认值,如果为空则返回空Map(可编辑)
     *
     * @param map 当前值 
     * @return 非null的Map
     * @param <K> 键类型 
     * @param <V> 值类型
     */
    public static <K, V> Map<K, V> defaultEditableMap(final Map<K, V> map) {
        if (null == map) {
            return new HashMap<K, V>();
        }
        return map;
    }

    /**
     * 获取默认值
     *
     * @param value 当前值 
     * @param defaultValue  默认值
     * @param <T> 列表类型
     * @return 参数值
     */
    public static <T> List<T> defaultValue(final List<T> value, final List<T> defaultValue) {
        if (CollectionUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取默认值
     *
     * @param value 当前值 
     * @param defaultValue  默认值
     * @param <T> 默认类型 
     * @return 参数值
     */
    public static <T> T defaultValue(final T value, final T defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取默认值
     *
     * @param value 当前值 
     * @param defaultValue  默认值
     * @return 参数值
     */
    public static String defaultValue(final String value, final String defaultValue) {
        if (StringUtil.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取第一个不为null的值
     *
     * @param <T> 类型
     * @param defaultValue 默认值
     * @param values 当前值
     * @return <T>
     */
    public static <T> T getFirstNotNull(final T defaultValue, final T... values) {
        if (values == null) {
            return defaultValue;
        }
        for (final T value : values) {
            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }

    /**
     * 默认的请求参数日志字符串
     *
     * @param request 请求参数
     * @param fields 重要参数列表
     * @return 请求参数日志字符串
     */
    public static String getDefaultParamLogStr(final Object request, final String... fields) {

        if (null == request) {
            return "请求参数为空";
        }

        if (ArrayUtil.isEmpty(fields)) {
            return request.toString();
        }

        final StringBuilder paramStr = new StringBuilder();
        for (final String field : fields) {
            final String getMethodName = makeGetMethod(field);
            try {
                final Method getMethod = ReflectionUtils.findMethod(request.getClass(),
                        getMethodName);
                paramStr.append(field).append("=").append(getMethod.invoke(request)).append(",");
            } catch (final Exception e) {
                //抛出异常
                throw new IllegalArgumentException("请求对象没有" + getMethodName, e);
            }
        }

        return paramStr.toString();
    }

    /**
     * 获取get方法
     *
     * @param field 属性
     * @return get方法
     */
    private static String makeGetMethod(final String field) {
        AssertUtil.notBlank(field, "获取默认日志, 字段不能为空!");
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

}
