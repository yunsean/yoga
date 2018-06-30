package com.yoga.content.template.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class UpdateDto extends TenantDto {

	@NotNull(message = "模板ID不能为空")
	private Long id;
	@NotNull(message = "模板编码不能为空")
	private String code;
	@NotNull(message = "模板名称不能为空")
	private String name;
	private String remark;
	private boolean enabled = true;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
