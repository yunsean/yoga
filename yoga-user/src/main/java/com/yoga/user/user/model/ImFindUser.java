package com.yoga.user.user.model;

/**
 * Created on 2017/4/27
 **/

public class ImFindUser {
    private Long userId;
    private String username;

    private String portraitUri;

    public ImFindUser() {
    }

    public ImFindUser(Long userId, String username, String portraitUri) {

        this.userId = userId;
        this.username = username;
        this.portraitUri = portraitUri;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }
}
