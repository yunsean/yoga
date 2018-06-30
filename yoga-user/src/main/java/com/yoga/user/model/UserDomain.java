package com.yoga.user.model;

import com.yoga.user.user.enums.GenderType;

import javax.persistence.*;
import java.util.Date;


@Table(name = "s_user")
public class UserDomain{
	@Id
	private long id;

	@Column(name = "tenant_id")
	private long tenantId;

	private String username;
	private String password;

	@Column(name = "dept_id")
	private Long deptId;
	@Column(name = "duty_id")
	private Long dutyId;

	private String firstname;
	private String lastname;
	private String fullname;

	private String title;
	private String avatar;
	private String phone;
	private String email;
	private String qq;
	private String address;
	private String postcode;

	private String company;
	private String wechat;

	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "update_time")
	private Date updateTime;
	private boolean disabled;

	@Column(name = "birthday")
	private Date birthday;
	@Column(name = "expire")
	private Date expire;

	@Column(name = "ext_long")
	private Long extLong;
	@Column(name = "ext_text")
	private String extText;
	@Column(name = "ext_int")
	private Integer extInt;
	@Column(name = "ext_double")
	private Double extDouble;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private GenderType gender;



	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Long getDeptId() {
		return deptId == null ? 0L : deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getDutyId() {
		return dutyId;
	}
	public void setDutyId(Long dutyId) {
		this.dutyId = dutyId;
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

	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public void updateFullname() {
		this.fullname = (lastname == null ? "" : lastname) + (firstname == null ? "" : firstname);
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getExpire() {
		return expire;
	}
	public void setExpire(Date expire) {
		this.expire = expire;
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

	public GenderType getGender() {
		return gender;
	}
	public void setGender(GenderType gender) {
		this.gender = gender;
	}


}
