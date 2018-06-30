package com.yoga.pay.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

    SEQ_PAY_WATER_ALIPAY_ID("seq_pay_water_alipay_id", "支付宝流水Id"),
    SEQ_TRANSFER_WATER_ALIPAY_ID("seq_transfer_water_alipay_id", "支付宝转账流水Id"),
    SEQ_PAY_WATER_WXPAY_ID("seq_pay_water_wxpay_id", "微信流水Id");
    private final String code;
    private final String desc;
    SequenceNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return desc;
    }
}
