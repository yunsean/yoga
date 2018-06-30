package com.yoga.pay.wxpay.enums;

import com.yoga.core.data.BaseEnum;

public enum TradeType implements BaseEnum<String > {
    JSAPI("公众号支付"),
    NATIVE("原生扫码支付"),
    APP("APP支付"),
    MWEB("H5支付");
    private final String name;
    TradeType(String name) {
        this.name = name;
    }

    public String getCode() {
        return toString();
    }
    public String getName() {
        return name;
    }

}
