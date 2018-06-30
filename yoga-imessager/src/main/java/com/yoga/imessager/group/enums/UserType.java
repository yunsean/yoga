package com.yoga.imessager.group.enums;

import com.yoga.core.data.BaseEnum;

public enum UserType implements BaseEnum<String> {
    NORMAL("normal", "普通用户"),
    ADMIN("admin", "管理员");
    private final String code;
    private final String name;
    UserType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UserType getEnum(String code) {
        for (UserType status : UserType.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
}