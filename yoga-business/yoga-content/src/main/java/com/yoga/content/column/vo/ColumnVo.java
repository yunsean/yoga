package com.yoga.content.column.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ColumnVo {

    @ApiModelProperty("栏目ID")
    private Long id;
    @ApiModelProperty("栏目编码")
    private String code;
    @ApiModelProperty("栏目名称")
    private String name;
    @ApiModelProperty("父栏目ID")
    private Long parentId;
    @ApiModelProperty("是否启用")
    private Boolean enabled;
    @ApiModelProperty("模板ID")
    private Long templateId;
    @ApiModelProperty("文章数量")
    private Integer articleCount;
    @ApiModelProperty("栏目描述")
    private String remark;
    @ApiModelProperty("是否从列表隐藏")
    private Boolean hidden;
    @ApiModelProperty("子栏目数量")
    private Integer childrenCount;
}

