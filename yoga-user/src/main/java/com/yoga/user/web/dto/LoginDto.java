package com.yoga.user.web.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LoginDto extends TenantDto {

	@NotEmpty(message = "请输入用户名")
	private String username;
	@NotEmpty(message = "请输入密码")
	private String password;
	private String uri;
	private boolean remember;
	private boolean first = true;

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

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isRemember() {
		return remember;
	}
	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
}
