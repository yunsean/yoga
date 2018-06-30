package com.yoga.core.annex.qr.dto;

import com.yoga.core.annotation.Explain;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class ShowDto {

    @NotEmpty(message = "QR不能为空")
    @Size(min = 40, max = 40, message = "无效的二维码")
    private String code;
    @NotEmpty(message = "文档地址不能为空")
    private String url;
    @Explain("设置此参数后，后台将代为访问URL后返回内容")
    private boolean transfer = false;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isTransfer() {
        return transfer;
    }
    public void setTransfer(boolean transfer) {
        this.transfer = transfer;
    }
}
