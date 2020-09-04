package com.yoga.admin.tenant.dto;

import com.yoga.setting.model.SaveItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TenantSaveSettingsDto {

	@NotNull(message = "未选定租户")
	private Long tenantId;
	private List<SaveItem> settings;
}
