package com.yoga.content.comment.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListDto extends BaseDto {

	private String filter;
	private Boolean issued;
}
