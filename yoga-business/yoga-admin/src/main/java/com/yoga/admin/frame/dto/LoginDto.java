package com.yoga.admin.frame.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto extends BaseDto {

	@NotBlank(message = "请输入用户名")
	private String username;
	@NotBlank(message = "请输入密码")
	private String password;
	private String patchca;
	private String uri;
	private boolean rememberMe;
}
