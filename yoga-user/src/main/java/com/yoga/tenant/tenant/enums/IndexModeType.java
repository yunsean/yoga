package com.yoga.tenant.tenant.enums;

import com.google.common.base.Objects;
import com.yoga.core.data.BaseEnum;

public enum IndexModeType implements BaseEnum<String> {

    Auto("", "自动判断"),
    Admin("/admin", "管理网站"),
    Web("/web", "前端网站");
    private final String path;
    private final String name;
    IndexModeType(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public static IndexModeType getType(int code) {
        for (IndexModeType status : IndexModeType.values()) {
            if (Objects.equal(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return path;
    }
    @Override
    public String getName() {
        return name();
    }
}
