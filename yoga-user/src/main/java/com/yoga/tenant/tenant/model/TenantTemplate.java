package com.yoga.tenant.tenant.model;

import com.yoga.core.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "g_tenant_template")
public class TenantTemplate extends BaseModel {

	@Id
	private long id;
	private String name;
	@Column(name = "create_time")
	private Date createTime;
	private String remark;
	private String modules;

	public TenantTemplate() {
		super();
	}
	public TenantTemplate(String name, String remark) {
		this.name = name;
		this.createTime = new Date();
		this.remark = remark;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String[] getModules() {
		if (modules == null) return null;
		return modules.split("\\|");
	}
	public void setModules(String[] modules) {
		this.modules = "";
		for (int i = 0; i < modules.length; i++) {
			if (i == 0) this.modules = modules[i];
			else this.modules += "|" + modules[i];
		}
	}
}
