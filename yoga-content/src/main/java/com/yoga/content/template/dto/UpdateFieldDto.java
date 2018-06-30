package com.yoga.content.template.dto;

import com.yoga.content.template.enums.FieldType;
import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class UpdateFieldDto extends TenantDto {

	@NotNull(message = "字段ID不能为空")
	private Long id;
	private String code;
	private String name;
	private String hint;
	private Integer type;
	private String param;
	private String remark;
	private String placeholder;
	private Boolean enabled;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}

	public FieldType getType() {
		return FieldType.getEnum(type);
	}
	public void setType(Integer type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
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
