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
public class TenantSetting implements Serializable {
	private static final long serialVersionUID = 1L;

	@JSONField
	private String platformName;
	@JSONField
	private String footerRemark;
	@JSONField
	private String resourcePrefix;
	@JSONField
	private String loginBackUrl;
	@JSONField
	private String loginLogoUrl;
	@JSONField
	private String topImageUrl;
	@JSONField
	private String menuColor = "384552";
	@JSONField
	private String adminIcon;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
