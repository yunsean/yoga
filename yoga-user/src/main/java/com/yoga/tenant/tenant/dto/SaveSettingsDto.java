package com.yoga.tenant.tenant.dto;

import com.yoga.tenant.setting.model.SaveItem;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SaveSettingsDto {

	@NotNull(message = "未选定租户")
	private Long tenantId;
	private List<SaveItem> settings;

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public List<SaveItem> getSettings() {
		return settings;
	}
	public void setSettings(List<SaveItem> settings) {
		this.settings = settings;
	}
}
