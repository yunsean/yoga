package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SortDto extends BaseDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;
	@NotNull(message = "未指定排序值")
	private Integer sort;
}
