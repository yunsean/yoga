package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class VerifyDto extends TenantDto {

    @NotNull(message = "请输入顾问ID")
    private Long id;
    private boolean rejected;
    private String reason;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRejected() {
        return rejected;
    }
    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}
