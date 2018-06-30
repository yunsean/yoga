package com.yoga.ewedding.recharge.enums;

import com.yoga.core.data.BaseEnum;

public enum RechargeType implements BaseEnum<String> {

    monthly("按月"),
    quarterly("按季"),
    halfyear("半年"),
    yearly("按年");
    private final String desc;
    RechargeType(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return toString();
    }
    public String getName() {
        return desc;
    }
}
