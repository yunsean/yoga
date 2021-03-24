package com.yoga.admin.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.operator.user.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@ApiModel("验证码发送结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaVo {
    @ApiModelProperty(value = "验证码存根")
    private String uuid;
    @ApiModelProperty(value = "短信验证码")
    private String captcha;
}
