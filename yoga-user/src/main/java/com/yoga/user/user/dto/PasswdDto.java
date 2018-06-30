package com.yoga.user.user.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class PasswdDto {
    @NotEmpty(message = "请输入旧密码")
    private String oldPwd;
    @NotEmpty(message = "请输入新密码")
    private String newPwd;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
