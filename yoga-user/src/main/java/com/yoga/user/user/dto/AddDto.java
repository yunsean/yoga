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

public class AddDto extends TenantDto{

	@NotNull(message = "用户名不能为空")
	@Size(min = 0, max = 20, message = "用户名长度只能在1-20个字符之间")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="用户名只支持英文,数字,或者两者组合")
	private String username;
	@NotNull(message = "密码不能为空")
	@Size(min = 6, message = "密码至少需要6个字符")
	private String password;

	@Size(min = 0, max = 10, message = "姓长度只能在1-10个字符之间")
	private String lastname;
	@Size(min = 0, max = 10, message = "名长度只能在1-10个字符之间")
	private String firstname;

	private long deptId = 0;
	private long dutyId = 0;
	private long[] roleIds;

	private String avatar;
	private String phone;
	private String email;
	private String qq;
	private String title;
	private String address;
	private String postcode;
	private String company;
	private String wechat;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private Long extLong;
	private String extText;
	private Integer extInt;
	private Double extDouble;
	@Enumerated(EnumType.STRING)
	private GenderType gender = GenderType.unknown;

	public long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(long[] roleIds) {
		this.roleIds = roleIds;
	}

	public String getUsername() {
		return null != this.username ? this.username.toUpperCase() : null;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		if(null == password || "".equals(password.trim())) return "123456";
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public long getDutyId() {
		return dutyId;
	}

	public void setDutyId(long dutyId) {
		this.dutyId = dutyId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
}
