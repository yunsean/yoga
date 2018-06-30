package com.yoga.tenant.tenant.model;

import com.yoga.core.model.BaseModel;
import com.yoga.user.admin.menu.MenuItem;

import javax.persistence.*;

@Entity
@Table(name = "g_tenant_menu")
public class TenantMenu extends BaseModel {

	@Id
	private long id;
	@Column(name = "tenant_id")
	private long tenantId;
	@Column(name = "menu_code")
	private String code;
	@Column(name = "menu_group")
	private String group;
	private String name;
	private String url;
	private String remark;
	private int disabled;
	private int sort;

	public TenantMenu() {
	}

	public TenantMenu(long tenantId, String code, String group, String name, String url, String remark, int sort) {
		this.tenantId = tenantId;
		this.code = code;
		this.group = group;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.disabled = 0;
		this.sort = sort;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
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

	public boolean isDisabled() {
		return disabled != 0;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled ? 1 : 0;
	}

	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	public MenuItem asMenuItem() {
		return new MenuItem(this.id, this.sort, this.code, this.name, this.url, this.remark, !this.isDisabled());
	}
}
