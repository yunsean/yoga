package com.yoga.user.admin.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginDto extends TenantDto {

	@NotEmpty(message = "请输入用户名")
	private String username;
	@NotEmpty(message = "请输入密码")
	private String password;
	private String patchca;
	private String uri;
	private boolean rememberMe;

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

	public String getPatchca() {
		return patchca;
	}
	public void setPatchca(String patchca) {
		this.patchca = patchca;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
}
