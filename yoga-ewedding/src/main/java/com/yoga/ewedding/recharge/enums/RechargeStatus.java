package com.yoga.ewedding.recharge.enums;

import com.yoga.core.data.BaseEnum;

public enum RechargeStatus implements BaseEnum<String> {

    pay("待支付"),
    paied("已支付"),
    closed("已关闭"),
    refund("已退款");
    private final String desc;
    RechargeStatus(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return toString();
    }
    public String getName() {
        return desc;
    }
}
