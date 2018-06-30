package com.yoga.content.template.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class DelFieldDto extends TenantDto {

	@NotNull(message = "字段ID不能为空")
	private Long id;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
