package com.yoga.user.captcha.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class SaveSettingDto extends TenantDto {

	private String format;
	@Min(value = 4, message = "验证码长度不能小于4")
	@Max(value = 20, message = "验证码长度不能超过20")
	private int length;
	@Min(value = 5, message = "过期时间不能小于5秒")
	private int expire;
	private int interval;
	private boolean autofill;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isAutofill() {
		return autofill;
	}

	public void setAutofill(boolean autofill) {
		this.autofill = autofill;
	}
}
