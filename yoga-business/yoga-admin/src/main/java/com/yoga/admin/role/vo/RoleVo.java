package com.yoga.admin.role.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("角色信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVo {
    @ApiModelProperty(value = "角色ID")
    private long id;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "角色描述")
    private String remark;
}
