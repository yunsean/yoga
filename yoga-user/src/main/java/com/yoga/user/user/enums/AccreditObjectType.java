package com.yoga.user.user.enums;

import com.google.common.base.Objects;
import com.yoga.core.data.BaseEnum;

public enum AccreditObjectType implements BaseEnum<Integer> {

    DEPARTMENT(1),
    DUTY(2),
    USER(3);
    private final Integer code;
    private AccreditObjectType(Integer code) {
        this.code = code;
    }

    public static AccreditObjectType getType(int code) {
        for (AccreditObjectType status : AccreditObjectType.values()) {
            if (Objects.equal(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }
    public Integer getCode() {
        return code;
    }
    @Override
    public String getName() {
        return toString();
    }
}
