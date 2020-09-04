package com.yoga.admin.branch.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("部门信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchNodeVo {
    @ApiModelProperty("部门ID")
    private long id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("部门描述")
    private String remark;
    @ApiModelProperty("父级部门ID")
    private Long parentId;
    @ApiModelProperty("下级部门")
    private List<BranchNodeVo> children;
}
