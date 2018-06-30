package com.yoga.core.annex.qr.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class ChartDto {

    @NotEmpty(message = "二维码不能为空")
    private String code;
    private int width = 512;
    private int height = 512;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
}
