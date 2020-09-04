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

	@ApiModelProperty("分类ID")
	private Long id;
	@ApiModelProperty("分类编码")
	private String code;
	@ApiModelProperty("分类名称")
	private String name;
	@ApiModelProperty("父ID")
	private Long parentId;
	@ApiModelProperty("图标")
	private String poster;
	@ApiModelProperty("子节点")
	private List<PropertyVo> children;
}
