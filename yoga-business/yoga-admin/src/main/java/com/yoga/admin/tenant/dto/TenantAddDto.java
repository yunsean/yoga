package com.yoga.admin.tenant.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TenantAddDto extends BaseDto {

	@ApiModelProperty(value = "租户名称", required = true)
	@NotNull(message = "租户名称不能为空")
	@Size(min = 1, max = 100, message = "租户名称长度只能在1-100个字符之间")
	private String name;
	@ApiModelProperty(value = "租户编码，10字节以内", required = true)
	@NotNull(message = "租户编码不能为空")
	@Size(min = 1, max = 10, message = "租户code长度只能在1-10个字符之间")
	private String code;
	@ApiModelProperty(value = "模板编码，可选")
	private Long templateId;
	@ApiModelProperty(value = "租户描述信息，512字符以内")
	@Size(max = 512, message = "租户描述长度不能超过512个字符")
	private String remark;
	@ApiModelProperty(value = "管理员用户名，默认值admin")
	@NotBlank(message = "请填写管理员用户名")
	private String username = "admin";
	@ApiModelProperty(value = "管理员姓名，默认“管理员”")
	private String nickname = "管理员";
	@ApiModelProperty(value = "管理员初始密码，默认123456")
	@Size(min = 6, message = "密码长度不能小于6位")
	private String password = "123456";
	@ApiModelProperty(value = "管理员手机号", required = true)
	@NotBlank(message = "请输入管理员手机号")
	private String mobile = null;
}
