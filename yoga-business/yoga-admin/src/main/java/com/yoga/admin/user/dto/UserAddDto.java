package com.yoga.admin.user.dto;

import com.yoga.core.base.BaseDto;
import com.yoga.operator.user.enums.GenderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserAddDto extends BaseDto{

	@ApiModelProperty(value = "用户名", required = true)
	@NotNull(message = "用户名不能为空")
	@Size(min = 0, max = 20, message = "用户名长度只能在1-20个字符之间")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message="用户名只支持英文,数字,或者两者组合")
	private String username;
	@ApiModelProperty(value = "初始密码", required = true)
	@NotNull(message = "密码不能为空")
	@Size(min = 6, message = "密码至少需要6个字符")
	private String password;
	@ApiModelProperty(value = "别名")
	@Size(min = 0, max = 10, message = "别名只能在1-10个字符之间")
	private String nickname;
	@ApiModelProperty(value = "性别")
	@Enumerated(EnumType.STRING)
	private GenderType gender = GenderType.unknown;
	@ApiModelProperty(value = "所属部门")
	private Long branchId;
	@ApiModelProperty(value = "所属职级")
	private Long dutyId;
	@ApiModelProperty(value = "赋予角色列表")
	private Long[] roleIds;
	@ApiModelProperty(value = "用户头像")
	private String avatar;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "电子邮箱")
	private String email;
	@ApiModelProperty(value = "用户称谓")
	private String title;
	@ApiModelProperty(value = "详细地址")
	private String address;
	@ApiModelProperty(value = "邮编")
	private String postcode;
	@ApiModelProperty(value = "公司")
	private String company;
	@ApiModelProperty(value = "生日日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
}
