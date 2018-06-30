package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SaveSettingDto extends TenantDto {

	@NotNull(message = "未选定租户")
	private Long tenantId;
	private String platform;
	private String footer;
	private String resource;
	private String loginbg;
	private String loginlogo;
	private String topimage;
	private String role;
	private String dept;
	private String duty;

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getLoginbg() {
		return loginbg;
	}
	public void setLoginbg(String loginbg) {
		this.loginbg = loginbg;
	}

	public String getLoginlogo() {
		return loginlogo;
	}
	public void setLoginlogo(String loginlogo) {
		this.loginlogo = loginlogo;
	}

	public String getTopimage() {
		return topimage;
	}
	public void setTopimage(String topimage) {
		this.topimage = topimage;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
}
