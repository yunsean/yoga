package com.yoga.core.utils;

import com.yoga.core.base.BaseEnum;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * 
 * 
 * @author qu.raoq
 * @version $Id: EnumUtil.java, v 0.1 2016年9月20日 下午10:06:00 qu.raoq Exp $
 */
public class EnumUtil {

    /**获取Code方法名称  */
    private static final String GET_CODE_METHOD_NAME = "getCode";
    /**获取Text方法名称  */
    private static final String GET_TEXT_METHOD_NAME = "getText";

    /**
     * 通过枚举code获取枚举值,枚举必须含有getCode方法
     *
     * @param enumClass 枚举类
     * @param enumCode 枚举值
     * @param <E> 枚举类型
     * @param defaultValue 默认值
     * @return 找到的枚举
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E getEnumByCode(final Class<E> enumClass,
                                                      final String enumCode, final E defaultValue) {
        try {
            final Method getCode = enumClass.getMethod(GET_CODE_METHOD_NAME);
            final EnumSet<E> enumSet = EnumSet.allOf(enumClass);
            for (final Enum<E> e : enumSet) {
                if (StringUtil.equals(enumCode, String.valueOf(getCode.invoke(e)))) {
                    return (E) e;
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(String.format("枚举类型不正确:%s", enumClass), e);
        }
        return defaultValue;
    }

    /**
     * 通过枚举text获取枚举值,枚举必须含有getText方法
     *
     * @param enumClass 枚举类
     * @param enumCode 枚举值
     * @param <E> 枚举类型
     * @param defaultValue 默认值
     * @return 找到的枚举
     */
    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> E getEnumByText(final Class<E> enumClass,
                                                      final String enumCode, final E defaultValue) {
        try {
            final Method getCode = enumClass.getMethod(GET_TEXT_METHOD_NAME);
            final EnumSet<E> enumSet = EnumSet.allOf(enumClass);
            for (final Enum<E> e : enumSet) {
                if (StringUtil.equals(enumCode, String.valueOf(getCode.invoke(e)))) {
                    return (E) e;
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(String.format("枚举类型不正确:%s", enumClass), e);
        }
        return defaultValue;
    }

    /**
     * 通过枚举code获取枚举值,枚举必须含有getCode方法
     * 
     * @param enumClass 枚举类
     * @param enumCode 枚举值
     * @param <E> 枚举类型
     * @return 找到的枚举
     */
    public static <E extends Enum<E>> E getEnumByCode(final Class<E> enumClass,
                                                      final String enumCode) {
        return getEnumByCode(enumClass, enumCode, null);
    }

    public static <T extends BaseEnum<?>> Integer[] getCodeByEnum(final T[] values) {
        if (values == null) return null;
        return Arrays.stream(values).map(e-> e.getCode()).toArray(Integer[]::new);
    }
}
