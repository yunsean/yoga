package com.yoga.core.base;


import com.yoga.core.data.ChainMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@Data
public class BaseDto {

    @ApiModelProperty(hidden = true)
    @NotNull(message = "未指定租户信息")
    private Long tid = 0L;

    public ChainMap<String, Object> wrapAsMap() {
        ChainMap<String, Object> result = new ChainMap<>();
        try {
            Class<?> parameterType = getClass();
            for (; parameterType != BaseDto.class; parameterType = parameterType.getSuperclass()) {
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
