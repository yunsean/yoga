package com.yoga.ewedding.counselor.enums;


import com.yoga.core.data.BaseEnum;

public enum CounselorStatus implements BaseEnum<String> {

    filling("资料待完善"),
    checking("待审核"),
    rejected("被拒绝"),
    accepted("已通过");
    private final String desc;
    private CounselorStatus(String desc) {
        this.desc = desc;
    }
    public String getCode() {
        return toString();
    }
    @Override
    public String getName() {
        return desc;
    }
}
