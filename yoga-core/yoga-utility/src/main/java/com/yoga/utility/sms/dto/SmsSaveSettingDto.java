package com.yoga.utility.sms.dto;

import com.yoga.core.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsSaveSettingDto extends BaseDto {

	@NotEmpty(message = "请选择短信网关")
	private String service;
}
