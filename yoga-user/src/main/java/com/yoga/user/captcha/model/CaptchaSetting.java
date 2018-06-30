package com.yoga.user.captcha.model;

import com.yoga.user.basic.TenantDto;

public class CaptchaSetting extends TenantDto {

	private String format;
	private int length = 6;
	private int expire = 60;
	private int interval = 0;
	private boolean autofill = false;

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
