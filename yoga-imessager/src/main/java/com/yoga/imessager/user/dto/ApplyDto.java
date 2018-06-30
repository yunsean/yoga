package com.yoga.imessager.user.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class ApplyDto extends TenantDto {

    @Explain("申请加入的群组ID")
    @NotNull(message = "群组ID不能为空")
    private Long id;
    @Explain("发送给管理员的申请消息")
    private String message = "";

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
