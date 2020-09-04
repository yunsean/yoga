package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TenantAddMenuDto extends BaseDto {

	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull(message = "租户ID不能为空")
	private Long tenantId;
	@ApiModelProperty(value = "菜单的权限编码，25字符以内", required = true)
	@NotNull(message = "权限编码不能为空")
	@Size(min = 1, max = 25, message = "权限编码只能在1-25个字符之间")
	private String code;
	@ApiModelProperty(value = "菜单的分组名称（一级菜单），25字符以内", required = true)
	@NotNull(message = "板块不能为空")
	@Size(min = 1, max = 25, message = "板块长度只能在1-25个字符之间")
	private String group;
	@ApiModelProperty(value = "菜单名称（二级菜单），25字符以内", required = true)
	@NotNull(message = "菜单名称不能为空")
	@Size(min = 1, max = 25, message = "菜单名称长度只能在1-25个字符之间")
	private String name;
	@ApiModelProperty(value = "菜单连接地址，使用/作为根地址，512字符以内", required = true)
	@NotNull(message = "菜单地址不能为空")
	@Size(min = 1, max = 512, message = "菜单地址长度只能在1-512个字符之间")
	private String url;
	@ApiModelProperty(value = "菜单备注信息，512字符以内")
	@Size(max = 512, message = "备注信息长度不能超过512个字符")
	private String remark;
	@ApiModelProperty(value = "菜单排序值，数字越大越靠下")
	private int sort = 100;
}
