package com.yoga.admin.duty.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("职级信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DutyVo {
    @ApiModelProperty("职级ID")
    private long id;
    @ApiModelProperty("职级名称")
    private String name;
    @ApiModelProperty("职级编码")
    private String code;
    @ApiModelProperty("职级描述")
    private String remark;
    @ApiModelProperty("职级角色")
    private List<Long> roleIds;
}
