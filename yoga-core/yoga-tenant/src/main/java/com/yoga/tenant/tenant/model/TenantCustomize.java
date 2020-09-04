package com.yoga.tenant.tenant.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TenantCustomize implements Serializable {
	private static final long serialVersionUID = 1L;

	@JSONField
	private String adminLogin;
	@JSONField
	private String adminIndex;
	@JSONField
	private String adminLeft;
	@JSONField
	private String adminTop;
	@JSONField
	private String adminWelcome;
	@JSONField
	private String frontIndex;
	@JSONField
	private String frontLogin;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
