package com.yoga.tenant.tenant.util;

import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TenantContext {

    private static final Logger log = LoggerFactory.getLogger(ThreadContext.class);
    public static final String TENANT_SETTING = ThreadContext.class.getName() + "_TENANT_SETTING";
    public static final String TENANT_ID = ThreadContext.class.getName() + "_TENANT_ID";
    private static final ThreadLocal<Map<Object, Object>> resources = new TenantContext.InheritableThreadLocalMap();

    public static long getId() {
        Long tid = (Long) get(TENANT_ID);
        if (tid == null) return 0;
        else return tid;
    }
    public static void setId(long tenantId) {
        put(TENANT_ID, tenantId);
    }

    public static Map<String, Object> getSetting() {
        return (Map<String, Object>) get(TENANT_SETTING);
    }
    public static void setSetting(Map<String, Object> setting) {
        if (setting != null) {
            put(TENANT_SETTING, setting);
        }
    }

    private static Object get(Object key) {
        Map map = (Map) resources.get();
        return map.get(key);
    }
    private static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else if (value == null) {
            remove(key);
        } else {
            ((Map) resources.get()).put(key, value);
        }
    }
    private static Object remove(Object key) {
        Object value = ((Map) resources.get()).remove(key);
        return value;
    }

    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends InheritableThreadLocal<Map<Object, Object>> {
        protected Map<Object, Object> initialValue() {
            return new HashMap();
        }

        protected Map<Object, Object> childValue(Map<Object, Object> parentValue) {
            return parentValue != null ? (Map) ((HashMap) parentValue).clone() : null;
        }
    }
}
