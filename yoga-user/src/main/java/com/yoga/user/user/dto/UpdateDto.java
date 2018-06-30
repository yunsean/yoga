package com.yoga.user.user.dto;

import com.yoga.user.basic.TenantDto;
import com.yoga.user.user.enums.GenderType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class UpdateDto extends TenantDto {
    private Long id;

    @Size(min = 0, max = 20, message = "用户名长度只能在1-20个字符之间")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message="用户名只支持英文,数字,或者两者组合")
    private String username;
    private String avatar;

    private String firstname;
    private String lastname;

    private Long deptId;
    private String phone;

    private String email;
    private String qq;

    private String title;
    private String address;

    private String postcode;
    private String company;

    private long[] roleIds;
    private Long dutyId;

    private String password;

    private String wechat;

    private Long extLong;
    private String extText;
    private Integer extInt;
    private Double extDouble;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @Enumerated(EnumType.STRING)
    private GenderType gender = GenderType.unknown;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(long[] roleIds) {
        this.roleIds = roleIds;
    }

    public Long getDutyId() {
        return dutyId;
    }

    public void setDutyId(Long dutyId) {
        this.dutyId = dutyId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public Long getExtLong() {
        return extLong;
    }

    public void setExtLong(Long extLong) {
        this.extLong = extLong;
    }

    public String getExtText() {
        return extText;
    }

    public void setExtText(String extText) {
        this.extText = extText;
    }

    public Integer getExtInt() {
        return extInt;
    }

    public void setExtInt(Integer extInt) {
        this.extInt = extInt;
    }

    public Double getExtDouble() {
        return extDouble;
    }

    public void setExtDouble(Double extDouble) {
        this.extDouble = extDouble;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GenderType getGender() {
        return gender;
    }
    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
