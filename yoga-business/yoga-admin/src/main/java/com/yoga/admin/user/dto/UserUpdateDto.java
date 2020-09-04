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
public class UserUpdateDto extends BaseDto {

    @ApiModelProperty(value = "用户ID")
    private Long id;
    @Size(min = 0, max = 20, message = "用户名长度只能在1-20个字符之间")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message="用户名只支持英文,数字,或者两者组合")
    private String username;
    private String avatar;
    private String nickname;
    @Enumerated(EnumType.STRING)
    private GenderType gender = GenderType.unknown;
    private Long branchId;
    private Long dutyId;
    private String mobile;
    private String email;
    private String title;
    private String address;
    private String postcode;
    private String company;
    private Long[] roleIds;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}
