package com.yoga.admin.template.dto;

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
public class TemplateAddMenuDto extends BaseDto {

	@ApiModelProperty(value = "模板ID", required = true)
	@NotNull(message = "模板ID不能为空")
	private Long templateId;
	@ApiModelProperty(value = "菜单权限编码", required = true)
	@NotNull(message = "权限编码不能为空")
	@Size(min = 1, max = 25, message = "权限编码只能在1-25个字符之间")
	private String code;
	@ApiModelProperty(value = "菜单分组名称（一级分类）", required = true)
	@NotNull(message = "板块不能为空")
	@Size(min = 1, max = 25, message = "板块长度只能在1-25个字符之间")
	private String group;
	@ApiModelProperty(value = "菜单名称（二级分类）", required = true)
	@NotNull(message = "菜单名称不能为空")
	@Size(min = 1, max = 25, message = "菜单名称长度只能在1-25个字符之间")
	private String name;
	@ApiModelProperty(value = "菜单地址", required = true)
	@NotNull(message = "菜单地址不能为空")
	@Size(min = 1, max = 512, message = "菜单地址长度只能在1-512个字符之间")
	private String url;
	@ApiModelProperty(value = "菜单描述")
	@Size(max = 512, message = "备注信息长度不能超过512个字符")
	private String remark;
	@ApiModelProperty(value = "菜单排序，数字越大越靠后，默认1000")
	private int sort = 100;
}
