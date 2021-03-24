package com.yoga.content.template.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.content.template.enums.FieldType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TemplateFieldVo {

	@ApiModelProperty(value = "字段ID")
	private Long id;
	@ApiModelProperty(value = "字段名称")
	private String name;
	@ApiModelProperty(value = "字段编码")
	private String code;
	@ApiModelProperty(value = "字段类型")
	private FieldType type;
	@ApiModelProperty(value = "字段取值")
	private String param;
	@ApiModelProperty(value = "字段提示")
	private String hint;
	@ApiModelProperty(value = "字段描述")
	private String remark;
	@ApiModelProperty(value = "缺省值")
	private String placeholder;
	@ApiModelProperty(value = "是否启用")
	private Boolean enabled;
}

