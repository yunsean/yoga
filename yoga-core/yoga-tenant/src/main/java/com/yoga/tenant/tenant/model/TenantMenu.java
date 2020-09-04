package com.yoga.tenant.tenant.model;

import com.yoga.tenant.menu.MenuItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_tenant_menu")
public class TenantMenu {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "code")
	private String code;
	@Column(name = "`group`")
	private String group;
	@Column(name = "`name`")
	private String name;
	private String url;
	private String remark;
	private Boolean enabled;
	@Column(name = "`sort`")
	private Integer sort;

	public TenantMenu(long tenantId, String code, String group, String name, String url, String remark, int sort) {
		this.tenantId = tenantId;
		this.code = code;
		this.group = group;
		this.name = name;
		this.url = url;
		this.remark = remark;
		this.enabled = true;
		this.sort = sort;
	}

	public MenuItem asMenuItem() {
		return new MenuItem(this.id, this.sort, this.code, this.name, this.url, this.remark, this.getEnabled());
	}
}
