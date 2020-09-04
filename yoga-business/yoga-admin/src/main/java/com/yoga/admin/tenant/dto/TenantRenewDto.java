package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TenantRenewDto extends BaseDto {

	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull(message = "请选择需要恢复的租户")
	private Long id;
	@ApiModelProperty(value = "恢复后的租户编码", required = true)
	@NotNull(message = "租户编码不能为空")
	@Size(min = 1, max = 10, message = "租户code长度只能在1-10个字符之间")
	private String code;
}
