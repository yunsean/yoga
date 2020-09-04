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

	@ApiModelProperty("字段ID")
	private Long id;
	@ApiModelProperty("字段名称")
	private String name;
	@ApiModelProperty("字段编码")
	private String code;
	@ApiModelProperty("字段类型")
	private FieldType type;
	@ApiModelProperty("字段取值")
	private String param;
	@ApiModelProperty("字段提示")
	private String hint;
	@ApiModelProperty("字段描述")
	private String remark;
	@ApiModelProperty("缺省值")
	private String placeholder;
	@ApiModelProperty("是否启用")
	private Boolean enabled;
}

