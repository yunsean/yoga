package com.yoga.core.annex.qr.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class TransferDto {

    @NotEmpty(message = "QR不能为空")
    @Size(min = 40, max = 40, message = "无效的二维码")
    private String code;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
