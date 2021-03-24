package com.yoga.admin.branch.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("部门信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchVo {
    @ApiModelProperty(value = "部门ID")
    private long id;
    @ApiModelProperty(value = "部门名称")
    private String name;
    @ApiModelProperty(value = "部门描述")
    private String remark;
    @ApiModelProperty(value = "父级部门ID")
    private Long parentId;
    @ApiModelProperty(value = "部门角色")
    private List<Long> roleIds;
}
