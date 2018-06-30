package com.yoga.content.template.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yoga.content.template.enums.FieldType;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "cmsTemplateField")
@Table(name = "cms_template_field")
@JsonInclude(Include.NON_NULL)
public class TemplateField implements Serializable{
	private static final long serialVersionUID = 3611898658311526077L;

	@Id
	private long id;
	
	@Column(name = "tenant_id")
	private long tenantId;
	
	@Column(name="template_id")	
	private long templateId;

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
	private boolean enabled;

	public TemplateField() {
		super();
	}
	public TemplateField(long tenantId, long templateId, String name, String code, FieldType type, String param, String hint, String remark, String placeholder, boolean enabled) {
		super();
		this.tenantId = tenantId;
		this.templateId = templateId;
		this.name = name;
		this.code = code;
		this.type = type;
		this.param = param;
		this.remark = remark;
		this.placeholder = placeholder;
		this.hint = hint;
		this.enabled = enabled;
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

	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
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

	public FieldType getType() {
		return type;
	}
	public void setType(FieldType type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}

	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}

