package com.yoga.utility.sms.dto;

import com.yoga.core.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsShowSettingDto extends BaseDto {

	private String service;
}
