package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.RegEx;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class TenantSaveSettingDto extends BaseDto {

	@NotNull(message = "请选择租户")
	private Long tenantId;
	private String platform;
	private String footer;
	private String resource;
	private String loginbg;
	private String loginlogo;
	private String topimage;
	@Pattern(regexp = "[0-9a-fA-F]{6}", message = "请输入6位16进制颜色值")
	private String menuColor;
	private String adminIcon;
}
