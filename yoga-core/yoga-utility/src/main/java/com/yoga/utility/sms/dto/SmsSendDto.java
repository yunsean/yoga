package com.yoga.utility.sms.dto;

import com.yoga.core.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDto extends BaseDto {

	@NotBlank(message = "请输入目标手机号码")
	private String mobile;
	@NotBlank(message = "请输入消息内容")
	private String content;
}
