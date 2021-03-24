package com.yoga.admin.user.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.operator.user.enums.GenderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("管理员信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo {
    @ApiModelProperty(value = "管理员")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "部门ID")
    private Long branchId;
    @ApiModelProperty(value = "职级ID")
    private Long dutyId;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "性别")
    @Enumerated(EnumType.STRING)
    private GenderType gender;
    @ApiModelProperty(value = "称谓")
    private String title;
    @ApiModelProperty(value = "头像URL")
    private String avatar;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "Email")
    private String email;
    @ApiModelProperty(value = "联系地址")
    private String address;
    @ApiModelProperty(value = "邮编")
    private String postcode;
    @ApiModelProperty(value = "公司")
    private String company;
    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;
    @ApiModelProperty(value = "部门")
    private String branch;
    @ApiModelProperty(value = "职级")
    private String duty;
    @ApiModelProperty(value = "职级")
    private Integer level;
    @ApiModelProperty(value = "授予角色")
    private List<Long> roleIds;
}
