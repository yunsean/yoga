package com.yoga.tenant.tenant.model;

import com.yoga.core.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "g_tenant")
public class Tenant extends BaseModel {

	@Id
	private long id;
	private String name;
	private String code;
	@Column(name = "create_time")
	private Date createTime;
	private String remark;
	private int deleted;
	@Column(name = "orgin_code")
	private String orginCode;
	private String modules;
	@Column(name = "template_id")
	private long templateId;
	@Column(name = "expire_date")
	private Date expireDate;

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

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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

	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId == null ? 0L : templateId;
	}

	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public boolean hasExpired() {
		if (expireDate == null) return true;
		if (expireDate.before(new Date())) return true;
		else return false;
	}

	public boolean getDeleted() {
		return deleted != 0;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted ? 1 : 0;
		if (deleted) this.code = String.valueOf(this.id);
	}

	public String getOrginCode() {
		return orginCode;
	}
	public void setOrginCode(String orginCode) {
		this.orginCode = orginCode;
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
