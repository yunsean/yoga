package com.yoga.imessager.user.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "imUser")
@Table(name = "im_user")
public class User {

    @Id
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "token")
    private String token;

    @Column(name = "avatar")
    private String avatar;

    public User() {
    }
    public User(Long id, Long tenantId, String nickname, String avatar) {
        this.id = id;
        this.tenantId = tenantId;
        if (nickname == null) this.nickname = "";
        else this.nickname = nickname;
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
