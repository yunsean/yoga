package com.yoga.pay.alipay.enums;

import com.yoga.core.data.BaseEnum;

public enum WaterType implements BaseEnum<String> {

    pay("支付"),
    recharge("充值"),
    refund("退款"),
    cancel("撤销"),
    close("关闭");
    private final String desc;
    WaterType(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return toString();
    }
    public String getName() {
        return desc;
    }
}
