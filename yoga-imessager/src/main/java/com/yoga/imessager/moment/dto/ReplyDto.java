package com.yoga.imessager.moment.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ReplyDto extends TenantDto {

    @NotNull(message = "消息ID不能为空")
    private Long momentId;
    @NotEmpty(message = "回复内容不能为空")
    private String content;
    private Long receiverId;

    public Long getMomentId() {
        return momentId;
    }
    public void setMomentId(Long momentId) {
        this.momentId = momentId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Long getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
