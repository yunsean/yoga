package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddDto extends BaseDto {

	@NotNull(message = "未指定栏目")
	private Long columnId;
	private String templateCode;
	@NotNull(message = "未指定标题")
	private String title;
}
