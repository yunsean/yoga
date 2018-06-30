package com.yoga.core.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

    SEQ_LOG_BEHAVIOR_ID("seq_log_behavior_id", "用户行为ID");

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
