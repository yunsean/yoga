package com.yoga.ewedding.recharge.enums;

import com.yoga.core.data.BaseEnum;

public enum PayType implements BaseEnum<String> {

    Alipay("支付宝"),
    Wechat("微信支付");
    private final String desc;
    PayType(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return toString();
    }
    public String getName() {
        return desc;
    }
}
