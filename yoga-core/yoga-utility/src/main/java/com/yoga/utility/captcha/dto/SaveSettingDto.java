package com.yoga.utility.captcha.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class SaveSettingDto extends BaseDto {

	private String format;
	@Min(value = 4, message = "验证码长度不能小于4")
	@Max(value = 20, message = "验证码长度不能超过20")
	private int length;
	@Min(value = 5, message = "过期时间不能小于5秒")
	private int expire;
	private int interval;
	private boolean autofill = false;
}
