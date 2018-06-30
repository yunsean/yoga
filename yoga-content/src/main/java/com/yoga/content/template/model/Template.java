package com.yoga.content.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "cmsTemplate")
@Table(name = "cms_template")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Template implements Serializable{
	private static final long serialVersionUID = 3611898658311526077L;

	@Id
	private long id;

	@Column(name = "tenant_id")
	private long tenantId;
	
	@Column(name="name")
	private String name;

	@Column(name="remark")
	private String remark;

	@Column(name="code")
	private String code;
	
	@Column(name = "is_enable")
	private boolean isEnabled;
	
	
	public Template() {
		super();
	}
	public Template(long tenantId, String name, String remark, String code, boolean isEnabled) {
		super();
		this.tenantId = tenantId;
		this.name = name;
		this.remark = remark;
		this.code = code;
		this.isEnabled = isEnabled;
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
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}

