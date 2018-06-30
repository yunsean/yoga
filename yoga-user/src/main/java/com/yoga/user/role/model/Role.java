package com.yoga.user.role.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "s_role")
public class Role {
	
	@Id
	private long id;

	@Column(name = "tenant_id")
	private long tenantId;
	
	private String code;
	
	private String name;
	
	private String remark;

	private int disabled;
	
	@Column(name = "create_time")
	private Date createTime;

	public Role() {

	}

	public Role(long tenantId, String code, String name, String remark) {
		this.tenantId = tenantId;
		this.code = code;
		this.name = name;
		this.remark = remark;
		this.disabled = 0;
		this.createTime = new Date();
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

	public boolean isDisabled() {
		return disabled != 0;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled ? 1 : 0;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
