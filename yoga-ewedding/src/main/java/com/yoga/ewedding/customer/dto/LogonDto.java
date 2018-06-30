package com.yoga.ewedding.customer.dto;

import com.yoga.user.basic.TenantDto;
import com.yoga.user.user.enums.GenderType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class LogonDto extends TenantDto {

    @NotNull(message = "手机号不能为空")
    private String mobile;
    @NotNull(message = "密码不能为空")
    @Size(min = 6, message = "密码至少需要6个字符")
    private String password;
    private String uuid;
    private String captcha;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date marryDate;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private GenderType gender = GenderType.unknown;
    private String nickname;

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCaptcha() {
        return captcha;
    }
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Date getMarryDate() {
        return marryDate;
    }
    public void setMarryDate(Date marryDate) {
        this.marryDate = marryDate;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public GenderType getGender() {
        return gender;
    }
    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
