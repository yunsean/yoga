package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDto extends BaseDto {

	@NotNull(message = "未指定文章ID")
	private String id;
	private Long columnId;
	private String templateCode;
	private String title;
}
