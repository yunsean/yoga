package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;
import com.yoga.user.user.enums.GenderType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LogonDto extends TenantDto {

    @NotEmpty(message = "请输入手机号")
    private String mobile;
    @NotEmpty(message = "请获取验证码")
    private String uuid;
    @NotEmpty(message = "请输入验证码")
    private String captcha;
    @Size(min = 6, max = 20, message = "密码不能少于6字符")
    private String password;
    @NotNull(message = "请选择顾问类型")
    private Long typeId;
    private String lastname;
    private String firstname;
    private String avatar;
    private String email;
    private String qq;
    private String title;
    private String company;
    private String wechat;
    private String intro;
    @Enumerated(EnumType.STRING)
    private GenderType gender = GenderType.unknown;

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }
    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getWechat() {
        return wechat;
    }
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getIntro() {
        return intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }

    public GenderType getGender() {
        return gender;
    }
    public void setGender(GenderType gender) {
        this.gender = gender;
    }
}
