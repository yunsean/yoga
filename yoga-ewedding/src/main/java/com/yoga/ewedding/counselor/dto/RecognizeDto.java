package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class RecognizeDto extends TenantDto{

    @NotEmpty(message = "请输入身份证正面图片URL")
    private String pidFront;

    public String getPidFront() {
        return pidFront;
    }
    public void setPidFront(String pidFront) {
        this.pidFront = pidFront;
    }
}
