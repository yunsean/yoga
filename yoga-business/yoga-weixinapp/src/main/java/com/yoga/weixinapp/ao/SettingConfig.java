package com.yoga.weixinapp.ao;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingConfig {

	@JSONField
	private String appId;
	@JSONField
	private String appSecret;
}
