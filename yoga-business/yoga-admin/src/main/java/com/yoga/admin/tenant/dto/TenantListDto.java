package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TenantListDto extends BaseDto {
	@ApiModelProperty(value = "查询特定租户模板")
	private Long templateId = null;
	@ApiModelProperty(value = "查询租户名称")
	private String name = null;
	@ApiModelProperty(value = "查询租户编码，全字匹配")
	private String code = null;
}
