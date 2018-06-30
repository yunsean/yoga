package com.yoga.tenant.tenant.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.yoga.core.model.BaseModel;


public class TenantCustomize extends BaseModel {

	@JSONField
	private String adminLogin;
	@JSONField
	private String adminIndex;
	@JSONField
	private String adminLeft;
	@JSONField
	private String adminTop;
	@JSONField
	private String adminWelcome;
	@JSONField
	private String frontIndex;
	@JSONField
	private String frontLogin;


	public String getFrontLogin() {
		return frontLogin;
	}

	public void setFrontLogin(String frontLogin) {
		this.frontLogin = frontLogin;
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
