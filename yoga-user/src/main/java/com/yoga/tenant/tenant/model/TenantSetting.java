package com.yoga.tenant.tenant.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.yoga.core.model.BaseModel;


public class TenantSetting extends BaseModel {

	@JSONField
	private String platformName;
	@JSONField
	private String footerRemark;
	@JSONField
	private String resourcePrefix;
	@JSONField
	private String loginBackUrl;
	@JSONField
	private String loginLogoUrl;
	@JSONField
	private String topImageUrl;
	@JSONField
	private String roleAlias = "角色";
	@JSONField
	private String deptAlias = "部门";
	@JSONField
	private String dutyAlias = "职务";

	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getFooterRemark() {
		return footerRemark;
	}
	public void setFooterRemark(String footerRemark) {
		this.footerRemark = footerRemark;
	}

	public String getResourcePrefix() {
		return resourcePrefix;
	}
	public void setResourcePrefix(String resourcePrefix) {
		this.resourcePrefix = resourcePrefix;
	}

	public String getLoginBackUrl() {
		return loginBackUrl;
	}
	public void setLoginBackUrl(String loginBackUrl) {
		this.loginBackUrl = loginBackUrl;
	}

	public String getLoginLogoUrl() {
		return loginLogoUrl;
	}
	public void setLoginLogoUrl(String loginLogoUrl) {
		this.loginLogoUrl = loginLogoUrl;
	}

	public String getTopImageUrl() {
		return topImageUrl;
	}
	public void setTopImageUrl(String topImageUrl) {
		this.topImageUrl = topImageUrl;
	}

	public String getRoleAlias() {
		return roleAlias;
	}
	public void setRoleAlias(String roleAlias) {
		this.roleAlias = roleAlias;
	}

	public String getDeptAlias() {
		return deptAlias;
	}
	public void setDeptAlias(String deptAlias) {
		this.deptAlias = deptAlias;
	}

	public String getDutyAlias() {
		return dutyAlias;
	}
	public void setDutyAlias(String dutyAlias) {
		this.dutyAlias = dutyAlias;
	}
}
