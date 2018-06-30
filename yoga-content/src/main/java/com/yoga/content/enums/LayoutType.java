package com.yoga.content.enums;

import com.yoga.core.data.BaseEnum;

public enum LayoutType implements BaseEnum<Integer> {
    LIST(1, "列表布局"),
    DETAIL(2, "详情布局");

    private final int code;
    private final String name;

    LayoutType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getName() {
        return name;
    }
}
