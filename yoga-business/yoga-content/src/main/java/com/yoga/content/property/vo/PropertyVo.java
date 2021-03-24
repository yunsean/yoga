package com.yoga.content.property.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PropertyVo {

	@ApiModelProperty(value = "分类ID")
	private Long id;
	@ApiModelProperty(value = "分类编码")
	private String code;
	@ApiModelProperty(value = "分类名称")
	private String name;
	@ApiModelProperty(value = "父ID")
	private Long parentId;
	@ApiModelProperty(value = "图标")
	private String poster;
	@ApiModelProperty(value = "子节点")
	private List<PropertyVo> children;
}
