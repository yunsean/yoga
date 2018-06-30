package com.yoga.tenant.tenant.dto;

import com.yoga.core.model.BaseModel;

import javax.validation.constraints.NotNull;


public class SaveCustomizeDto extends BaseModel {

	@NotNull(message = "未选定租户")
	private Long tenantId;
	private String adminLogin;
	private String adminIndex;
	private String adminLeft;
	private String adminTop;
	private String adminWelcome;
	private String frontIndex;
	private String frontLogin;

	public String getFrontLogin() {
		return frontLogin;
	}

	public void setFrontLogin(String frontLogin) {
		this.frontLogin = frontLogin;
	}

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getAdminLogin() {
		return adminLogin;
	}
	public void setAdminLogin(String adminLogin) {
		this.adminLogin = adminLogin;
	}

	public String getAdminIndex() {
		return adminIndex;
	}
	public void setAdminIndex(String adminIndex) {
		this.adminIndex = adminIndex;
	}

	public String getAdminLeft() {
		return adminLeft;
	}
	public void setAdminLeft(String adminLeft) {
		this.adminLeft = adminLeft;
	}

	public String getAdminTop() {
		return adminTop;
	}
	public void setAdminTop(String adminTop) {
		this.adminTop = adminTop;
	}

	public String getAdminWelcome() {
		return adminWelcome;
	}
	public void setAdminWelcome(String adminWelcome) {
		this.adminWelcome = adminWelcome;
	}

	public String getFrontIndex() {
		return frontIndex;
	}
	public void setFrontIndex(String frontIndex) {
		this.frontIndex = frontIndex;
	}
}
