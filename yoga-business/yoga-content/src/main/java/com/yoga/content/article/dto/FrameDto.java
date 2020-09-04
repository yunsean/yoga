package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FrameDto extends BaseDto {

	private long columnId = 0L;
	private String columnCode;
	private long parentId = 0L;
	private String parentCode;
}
