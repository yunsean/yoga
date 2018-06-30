package com.yoga.imessager.moment.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class UpvoteDto extends TenantDto {

    @NotNull(message = "消息ID不能为空")
    private Long momentId;

    public Long getMomentId() {
        return momentId;
    }
    public void setMomentId(Long momentId) {
        this.momentId = momentId;
    }
}
