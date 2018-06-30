package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.Min;

public class TemplateMenuDto extends TenantDto {

	@Min(value = 1, message = "菜单ID不能为0")
	private long menuId;

	public long getMenuId() {
		return menuId;
	}
	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
}
