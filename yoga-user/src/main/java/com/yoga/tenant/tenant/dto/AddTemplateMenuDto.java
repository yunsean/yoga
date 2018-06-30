package com.yoga.tenant.tenant.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AddTemplateMenuDto extends TenantDto {

	@NotNull(message = "模板ID不能为空")
	private Long templateId;
	@NotNull(message = "权限编码不能为空")
	@Size(min = 1, max = 25, message = "权限编码只能在1-25个字符之间")
	private String code;
	@NotNull(message = "板块不能为空")
	@Size(min = 1, max = 25, message = "板块长度只能在1-25个字符之间")
	private String group;
	@NotNull(message = "菜单名称不能为空")
	@Size(min = 1, max = 25, message = "菜单名称长度只能在1-25个字符之间")
	private String name;
	@NotNull(message = "菜单地址不能为空")
	@Size(min = 1, max = 512, message = "菜单地址长度只能在1-512个字符之间")
	private String url;
	@Size(max = 512, message = "备注信息长度不能超过512个字符")
	private String remark;
	private int sort = 100;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}
