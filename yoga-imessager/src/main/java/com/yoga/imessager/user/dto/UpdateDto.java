package com.yoga.imessager.user.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

public class UpdateDto extends TenantDto {

    @Explain("头像")
    private String avatar;
    @Explain("昵称")
    private String nickname;

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
