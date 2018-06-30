package com.yoga.user.user.enums;

import com.google.common.base.Objects;
import com.yoga.core.data.BaseEnum;

public enum GenderType implements BaseEnum<Integer> {

    unknown(-1, ""),
    male(0, "男"),
    female(1, "女");
    private final Integer code;
    private final String desc;
    GenderType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GenderType getType(int code) {
        for (GenderType status : GenderType.values()) {
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
        return desc;
    }
}
