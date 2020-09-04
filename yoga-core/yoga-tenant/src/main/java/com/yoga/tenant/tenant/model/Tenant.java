package com.yoga.tenant.tenant.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_tenant")
public class Tenant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	private Boolean template;
	private String name;
	private String code;
	private String remark;
	@Column(name = "orgin_code")
	private String orginCode;
	private String modules;
	@Column(name = "template_id")
	private Long templateId;
	@Column(name = "expire_date")
	private Date expireDate;
	private Boolean deleted;
	@Column(name = "create_time")
	private Date createTime;

	public Tenant(String name, String code, String remark, Long templateId, Date expireDate) {
		this.template = false;
		this.name = name;
		this.code = code;
		this.remark = remark;
		this.orginCode = code;
		this.modules = null;
		this.templateId = templateId;
		this.expireDate = expireDate;
		this.deleted = false;
		this.createTime = new Date();
	}
	public Tenant(String name, String remark) {
		this.template = true;
		this.name = name;
		this.remark = remark;
		this.modules = null;
		this.deleted = false;
		this.createTime = new Date();
	}

	public String[] listModules() {
		if (modules == null) return new String[]{};
		return modules.split("\\|");
	}
	public void saveModules(String[] modules) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < modules.length; i++) {
			if (i == 0) sb.append(modules[i]);
			else sb.append("|").append(modules[i]);
		}
		this.modules = sb.toString();
	}
}
