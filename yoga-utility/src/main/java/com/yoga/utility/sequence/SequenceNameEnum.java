package com.yoga.utility.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

	SEQ_U_JOURNAL_ID("seq_u_journal_id", "系统日志Id");
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
