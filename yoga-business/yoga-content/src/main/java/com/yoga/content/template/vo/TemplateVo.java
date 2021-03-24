package com.yoga.content.template.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TemplateVo {

	@ApiModelProperty(value = "模板ID")
	private Long id;
	@ApiModelProperty(value = "模板名称")
	private String name;
	@ApiModelProperty(value = "模板编码")
	private String code;
	@ApiModelProperty(value = "是否启用")
	private Boolean enabled;
	@ApiModelProperty(value = "字段数量")
	private Integer fieldCount;
	@ApiModelProperty(value = "模板备注")
	private String remark;
}

