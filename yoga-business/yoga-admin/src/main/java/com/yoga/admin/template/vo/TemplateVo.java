package com.yoga.admin.template.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("模板信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateVo {
    @ApiModelProperty("模板ID")
    private long id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("模板描述")
    private String remark;
}
