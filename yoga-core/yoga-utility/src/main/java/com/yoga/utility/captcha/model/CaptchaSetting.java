package com.yoga.utility.captcha.model;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CaptchaSetting extends BaseDto {

	private String format;
	private int length = 6;
	private int expire = 60;
	private int interval = 0;
	private boolean autofill = false;
}
