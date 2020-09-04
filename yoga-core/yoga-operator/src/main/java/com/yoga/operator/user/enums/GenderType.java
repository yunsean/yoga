package com.yoga.operator.user.enums;

import com.google.common.base.Objects;
import com.yoga.core.base.BaseEnum;

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

    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getName() {
        return desc;
    }
}
