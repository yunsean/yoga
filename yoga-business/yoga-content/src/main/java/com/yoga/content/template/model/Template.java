package com.yoga.content.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_template")
public class Template implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	
	@Column(name="name")
	private String name;
	@Column(name="remark")
	private String remark;
	@Column(name="code")
	private String code;
	@Column(name = "enabled")
	private Boolean enabled;

	@Transient
	private Integer fieldCount;

	public Template(Long tenantId, String name, String remark, String code, Boolean enabled) {
		super();
		this.tenantId = tenantId;
		this.name = name;
		this.remark = remark;
		this.code = code;
		this.enabled = enabled;
	}
}

