package com.yoga.admin.tenant.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("菜单信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenantMenuVo {
    @ApiModelProperty("菜单ID")
    private long id;
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("菜单编码")
    private String code;
    @ApiModelProperty("菜单分组")
    private String group;
    @ApiModelProperty("菜单URL")
    private String url;
    @ApiModelProperty("菜单描述")
    private String remark;
    @ApiModelProperty("菜单排序")
    private Integer sort;
}
