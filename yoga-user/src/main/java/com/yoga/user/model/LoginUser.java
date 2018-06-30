package com.yoga.user.model;

import org.apache.shiro.SecurityUtils;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1975029848142314313L;

    protected Long id;
    protected String username;
    protected String nickname;
    protected String avatar;
    protected String token;

    public LoginUser() {
        LoginUser user = null;
        try {
            user = (LoginUser) SecurityUtils.getSubject().getSession().getAttribute("login");
        } catch (Exception ex) {
        }
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
            this.avatar = user.getAvatar();
            this.token = user.getToken();
        }
    }
    public LoginUser(long id, String username, String nickname, String avatar, String token) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
        this.token = token;
    }

    public long getId() {
        return id == null ? 0L : id;
    }
    public String getUsername() {
        return username;
    }
    public String getNickname() {
        return nickname;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getToken() {
        return token;
    }

    public boolean isLogined() {
        return getId() != 0L;
    }
}
