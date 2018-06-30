package com.yoga.imessager.user.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

public class FindDto extends TenantDto {

    @Explain("昵称")
    private String nickname;

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
