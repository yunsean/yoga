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

    @ApiModelProperty(value = "栏目ID")
    private Long id;
    @ApiModelProperty(value = "栏目编码")
    private String code;
    @ApiModelProperty(value = "栏目名称")
    private String name;
    @ApiModelProperty(value = "父栏目ID")
    private Long parentId;
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
    @ApiModelProperty(value = "模板ID")
    private Long templateId;
    @ApiModelProperty(value = "文章数量")
    private Integer articleCount;
    @ApiModelProperty(value = "栏目描述")
    private String remark;
    @ApiModelProperty(value = "是否从列表隐藏")
    private Boolean hidden;
    @ApiModelProperty(value = "子栏目数量")
    private Integer childrenCount;
}

