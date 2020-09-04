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
public class TenantUpdateMenuDto extends BaseDto {

	@ApiModelProperty(value = "租户ID", required = true)
	@NotNull(message = "请指定租户")
	private Long tenantId;
	@ApiModelProperty(value = "待修改菜单ID", required = true)
	@Min(value = 1, message = "菜单ID不能为0")
	private long menuId;
	@ApiModelProperty(value = "菜单编码，25字符以内", required = true)
	@NotNull(message = "权限编码不能为空")
	@Size(min = 1, max = 25, message = "权限编码只能在1-25个字符之间")
	private String code;
	@ApiModelProperty(value = "分组名称，25字符以内", required = true)
	@NotNull(message = "板块不能为空")
	@Size(min = 1, max = 25, message = "板块长度只能在1-25个字符之间")
	private String group;
	@ApiModelProperty(value = "菜单名称，25字符以内", required = true)
	@NotNull(message = "菜单名称不能为空")
	@Size(min = 1, max = 25, message = "菜单名称长度只能在1-25个字符之间")
	private String name;
	@ApiModelProperty(value = "菜单地址，512字符以内", required = true)
	@NotNull(message = "菜单地址不能为空")
	@Size(min = 1, max = 512, message = "菜单地址长度只能在1-512个字符之间")
	private String url;
	@ApiModelProperty(value = "备注信息，512字符以内", required = true)
	@Size(max = 512, message = "备注信息长度不能超过512个字符")
	private String remark;
	@ApiModelProperty(value = "菜单排序值")
	private Integer sort;
}
