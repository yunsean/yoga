package com.yoga.content.column.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListDto extends BaseDto {

	private Long parentId;
	private String parentCode;
	private Boolean hidden;
}
