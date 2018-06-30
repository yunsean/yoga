package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;
import com.yoga.user.user.enums.GenderType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UpdateDto extends TenantDto {

    @NotEmpty(message = "请输入顾问身份信息")
    private String token;
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

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
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
