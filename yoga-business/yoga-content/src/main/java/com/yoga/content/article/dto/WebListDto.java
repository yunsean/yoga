package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WebListDto extends BaseDto {

	private String articleId;
	private Long columnId;
	private String columnCode;
	private String name;
	private boolean alone = false;		//是否独立页面（左侧没有栏目树)
}
