package com.yoga.ewedding.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

    SEQ_EW_CHARGE_ID("seq_ew_charge_id", "服务费用Id"),
    SEQ_EW_RECHARGE_ID("seq_ew_recharge_id", "充值Id");
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
