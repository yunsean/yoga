package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddTenantDto extends TenantDto {

	@NotNull(message = "租户名称不能为空")
	@Size(min = 1, max = 100, message = "租户名称长度只能在1-100个字符之间")
	private String name;

	@NotNull(message = "租户编码不能为空")
	@Size(min = 1, max = 10, message = "租户code长度只能在1-10个字符之间")
	private String code;

	@Size(max = 512, message = "租户描述长度不能超过512个字符")
	private String remark;

	private Long templateId;

	private String username = "admin";
	private String firstName = "理员";
	private String lastName = "管";
	private String password = "123456";
	private String phone = null;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
