package com.yoga.imessager.group.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.validation.constraints.NotNull;

public class Member {
    @JSONField
    @NotNull(message = "必须指定用户ID")
    private Long id;
    @JSONField
    private String nickname;
    @JSONField
    private String avatar;

    public Member() {

    }
    public Member(Long id, String nickname, String avatar) {
        this.id = id;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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
