package com.yoga.admin.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.operator.user.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("登录用户信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoVo {
    @ApiModelProperty("管理员")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("部门ID")
    private Long branchId;
    @ApiModelProperty("职级ID")
    private Long dutyId;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("性别")
    @Enumerated(EnumType.STRING)
    private GenderType gender;
    @ApiModelProperty("称谓")
    private String title;
    @ApiModelProperty("头像URL")
    private String avatar;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("Email")
    private String email;
    @ApiModelProperty("联系地址")
    private String address;
    @ApiModelProperty("邮编")
    private String postcode;
    @ApiModelProperty("公司")
    private String company;
    @ApiModelProperty("出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    @ApiModelProperty("部门")
    private String branch;
    @ApiModelProperty("职级")
    private String duty;
    @ApiModelProperty("赋予的权限")
    private Collection<String> permissions;
    @ApiModelProperty("Token")
    private String token;
}
