package com.yoga.core.annex.qr.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class QueryDto {

    @NotEmpty(message = "二维码不能为空")
    private String code;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
