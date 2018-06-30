package com.yoga.imessager.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

    SEQ_IM_GROUP_ID("seq_im_group_id", "群组Id"),
    SEQ_IM_MOMENT_ID("seq_im_moment_id", "朋友圈Id"),
    SEQ_IM_MOMENT_FOLLOW_ID("seq_im_moment_follow_id", "朋友圈回复Id");

    private final String code;
    private final String desc;
    private SequenceNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static SequenceNameEnum getEnum(String code) {
        for (SequenceNameEnum status : SequenceNameEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return desc;
    }
}
