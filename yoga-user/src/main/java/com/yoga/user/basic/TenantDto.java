package com.yoga.user.basic;

import com.yoga.core.annotation.Explain;
import com.yoga.core.utils.MapConverter;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

public class TenantDto {

    @Explain(exclude = true)
    @NotNull(message = "未指定租户信息")
    private Long tid = 0L;

    public long getTid() {
        if (tid == null) return 0;
        return tid;
    }
    public void setTid(Long tid) {
        this.tid = tid;
    }

    public MapConverter.MapItem<String, Object> wrapAsMap() {
        MapConverter.MapItem<String, Object> result = new MapConverter.MapItem<>();
        try {
            Class<?> parameterType = getClass();
            for (; parameterType != TenantDto.class; parameterType = parameterType.getSuperclass()) {
                Field[] fields = parameterType.getDeclaredFields();
                if (fields != null) {
                    for (Field field : fields) {
                        try {
                            boolean accessible = field.isAccessible();
                            field.setAccessible(true);
                            String name = field.getName();
                            Object value = field.get(this);
                            field.setAccessible(accessible);
                            result.put(name, value);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
