package com.yoga.admin.tenant.dto;
import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TenantSaveCustomizeDto extends BaseDto {

	@NotNull(message = "请选择租户")
	private Long tenantId;
	private String adminLogin;
	private String adminIndex;
	private String adminLeft;
	private String adminTop;
	private String adminWelcome;
	private String frontIndex;
	private String frontLogin;
}
