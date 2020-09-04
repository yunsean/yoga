package com.yoga.content.template.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yoga.content.template.enums.FieldType;
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
@Table(name = "cms_template_field")
public class TemplateField implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name="template_id")	
	private Long templateId;

	@Column(name="name")	
	private String name;
	@Column(name="code")	
	private String code;

	@Column(name="type")	
	@Enumerated(value = EnumType.ORDINAL)
	private FieldType type;
	@Column(name="param")	
	private String param;
	@Column(name="hint")	
	private String hint;
	@Column(name = "remark")
	private String remark;
	@Column(name = "placeholder")
	private String placeholder;
	@Column(name="is_enable")
	private Boolean enabled;
	@Column(name = "sort")
	private Integer sort;

	public TemplateField(Long templateId, String name, String code, FieldType type, String param, String hint, String remark, String placeholder, Boolean enabled, Integer sort) {
		super();
		this.templateId = templateId;
		this.name = name;
		this.code = code;
		this.type = type;
		this.param = param;
		this.remark = remark;
		this.placeholder = placeholder;
		this.hint = hint;
		this.enabled = enabled;
		this.sort = sort;
	}
}

