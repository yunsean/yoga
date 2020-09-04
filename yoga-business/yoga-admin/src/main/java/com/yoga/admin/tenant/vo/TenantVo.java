package com.yoga.admin.tenant.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("租户信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantVo {
    @ApiModelProperty("租户ID")
    private long id;
    @ApiModelProperty("租户名称")
    private String name;
    @ApiModelProperty("租户编码")
    private String code;
    @ApiModelProperty("租户描述")
    private String remark;
}
