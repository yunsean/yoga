/**
 * Alipay.com Inc. Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yoga.core.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;

/**
 * 断言工具类
 *
 * @author jian.gj
 * @version $Id: AssertUtil.java, v 0.1 2015年2月7日 下午1:26:21 jian.gj Exp $
 */
public class AssertUtil {
    /**
     * <p>断言对象为空，否则抛出传入的异常</p>
     *
     * @param str      断言字符串
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     */
    public static void notBlank(final String str, final String msgformat, final Object... formatArgs) {

        if (StringUtil.isBlank(str)) {

            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * <p>断言集合不为空或null，否则抛出传入的异常</p>
     *
     * @param collection 断言集合
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     *
     */
    public static void notEmpty(final Collection<?> collection, final String msgformat, final Object... formatArgs) {

        if (CollectionUtils.isEmpty(collection)) {

            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * <p>断言集合必须为空或null，否则抛出传入的异常</p>
     *
     * @param collection 断言集合
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     *
     */
    public static void isEmpty(final Collection<?> collection, final String msgformat, final Object... formatArgs) {
        isTrue(CollectionUtils.isEmpty(collection), msgformat, formatArgs);
    }

    /**
     * <p>断言对象非空字符串，否则抛出传入的异常</p>
     *
     * @param value   断言对象
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     */
    public static void notEmpty(final String value, final String msgformat, final Object... formatArgs) {
        isTrue(StringUtil.isNotEmpty(value), msgformat, formatArgs);
    }

    /**
     * <p>断言对象非null，否则抛出传入的异常</p>
     *
     * @param object   断言对象
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     *
     */
    public static void notNull(final Object object, final String msgformat, final Object... formatArgs) {

        if (null == object) {
            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz);</pre>
     * @param object the object to check
     * @throws IllegalArgumentException if the object is {@code null}
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * <p>断言对象非null，否则抛出传入的异常</p>
     *
     * @param object   断言对象
     * @param throwable 原始异常
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     *
     */
    public static void notNull(final Object object, final Throwable throwable, final String msgformat,
                               final Object... formatArgs) {

        if (null == object) {
            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs),
                    throwable);
        }
    }

    /**
     * <p>断言对象为null，否则抛出传入的异常</p>
     *
     * @param object   断言对象
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     *
     */
    public static void isNull(final Object object, final String msgformat, final Object... formatArgs) {
        isTrue(null == object, msgformat, formatArgs);
    }

    /**
     * <p>断言对象必须为真，否则抛出传入的异常</p>
     *
     * @param conf     断言条件
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     */
    public static void isTrue(final boolean conf, final String msgformat, final Object... formatArgs) {
        if (!conf) {

            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * <p>断言两个对象相同，否则抛出传入的异常。</p>
     *
     * @param first 对象1
     * @param second 对象2
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     * @param <T> 比较类型
     *
     * @see java.util.Formatter
     */
    public static <T> void equals(final T first, final T second, final String msgformat, final Object... formatArgs) {
        isTrue(ObjectUtil.equals(first, second),  msgformat, formatArgs);
    }

    /**
     * <p>断言两个对象不同，相同抛出传入的异常。</p>
     *
     * @param first 对象1
     * @param second 对象2
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     * @param <T> 比较类型
     *
     * @see java.util.Formatter
     */
    public static <T> void notEquals(final T first, final T second, final String msgformat, final Object... formatArgs) {

        isTrue(!ObjectUtil.equals(first, second), msgformat, formatArgs);
    }

    /**
     * <p>断言第一个整数必须小于第二个整数，否则抛出传入的异常</p>
     *
     * @param lessVal 断言对象1
     * @param largeVal 断言对象1
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     */
    public static void less(final long lessVal, final long largeVal, final String msgformat,
                            final Object... formatArgs) {

        if (lessVal >= largeVal) {
            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * <p>断言第一个整数必须小于等于第二个整数，否则抛出传入的异常</p>
     *
     * @param lessVal 较小的数
     * @param largeVal 较大的数
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     *
     * @see java.util.Formatter
     */
    public static void lessOrEq(final long lessVal, final long largeVal, final String msgformat,
                                final Object... formatArgs) {
        if (lessVal > largeVal) {
            throw new IllegalArgumentException(StringUtil.format(msgformat, formatArgs));
        }
    }

    /**
     * 断言值在枚举中或者为空
     *
     * @param enumClass 枚举类
     * @param enumCode 枚举值
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     * @param <E> 枚举类型
     */
    public static <E extends Enum<E>> void inEnumOrEmpty(final Class<E> enumClass,
                                                         final String enumCode,
                                                         final String msgformat,
                                                         final Object... formatArgs) {
        if (StringUtil.isNotEmpty(enumCode)) {
            notNull(EnumUtil.getEnumByCode(enumClass, enumCode), msgformat, formatArgs);
        }
    }

    /**
     * 断言值在枚举中
     *
     * @param enumClass 枚举类
     * @param enumCode 枚举值
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     * @param <E> 枚举类型
     */
    public static <E extends Enum<E>> void inEnum(final Class<E> enumClass, final String enumCode,
                                                  final String msgformat,
                                                  final Object... formatArgs) {
        notNull(enumCode, msgformat, formatArgs);
        notNull(EnumUtil.getEnumByCode(enumClass, enumCode), msgformat, formatArgs);
    }

    /**
     * 包含校验
     *
     * @param list 列表
     * @param value 待验证值
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     * @param <T> 列表元素类型 
     */
    public static <T> void contains(final Collection<T> list, final T value, final String msgformat,
                                    final Object... formatArgs) {

        isTrue(list.contains(value), msgformat, formatArgs);
    }

    /**
     * 断言类型存在继承关系或者相同
     *
     * @param superClass  父类
     * @param subclass    子类
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     */
    public static void assignableFrom(final Class<?> superClass, final Class<?> subclass,
                                      final String msgformat, final Object... formatArgs) {
        isTrue(superClass.isAssignableFrom(subclass), msgformat, formatArgs);
    }

    /**
     * 验证两个时间必须满足小于关系
     *
     * @param lessDate 较小的时间
     * @param largeDate 较大的时间
     * @param msgformat 错误消息(可格式化)
     * @param formatArgs 格式化参数
     */
    public static void after(final Date lessDate, final Date largeDate, final String msgformat,
                             final Object... formatArgs) {

        AssertUtil.notNull(lessDate,  msgformat, formatArgs);
        AssertUtil.notNull(largeDate, msgformat, formatArgs);
        AssertUtil.less(lessDate.getTime(), largeDate.getTime(), msgformat, formatArgs);
    }
}
