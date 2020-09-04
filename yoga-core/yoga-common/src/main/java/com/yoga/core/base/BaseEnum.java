package com.yoga.core.base;

import java.util.HashMap;
import java.util.Map;

public interface BaseEnum<T> {
    T getCode();
    String getName();
    static <E extends BaseEnum<T>, T> E getEnum(final Class<E> enumClass, final T enumValue) {
        if (enumValue == null) {
            return null;
        }
        try {
            return valueOf(enumClass, enumValue);
        } catch (final IllegalArgumentException ex) {
            return null;
        }
    }
    static <E extends BaseEnum<T>, T> E valueOf(Class<E> enumClass, T enumValue) {
        if (enumValue == null) throw new NullPointerException("EnumValue is null");
        return getEnumMap(enumClass).get(enumValue);
    }
    static <E extends BaseEnum<T>, T> Map<T, E> getEnumMap(Class<E> enumClass) {
        E[] enums = enumClass.getEnumConstants();
        if (enums == null)
            throw new IllegalArgumentException(enumClass.getSimpleName() + " does not represent an enum type.");
        Map<T, E> map = new HashMap<>(2 * enums.length);
        for (E t : enums){
            map.put(t.getCode(), t);
        }
        return map;
    }
}
