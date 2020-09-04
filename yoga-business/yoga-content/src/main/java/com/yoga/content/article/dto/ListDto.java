package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListDto extends BaseDto {

	private boolean onlineOnly = true;
	private Long columnId;

	private String columnCode;
	private String[] fields;

	private String name;
	private String code;

	private String templateId;
	private String type;
}
