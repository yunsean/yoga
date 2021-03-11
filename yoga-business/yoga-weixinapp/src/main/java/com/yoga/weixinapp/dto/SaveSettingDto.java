package com.yoga.weixinapp.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SaveSettingDto extends BaseDto {

	@NotBlank(message = "小程序ID不能为空")
	private String appId;
	@NotBlank(message = "小程序密匙不能为空")
	private String appSecret;
}
