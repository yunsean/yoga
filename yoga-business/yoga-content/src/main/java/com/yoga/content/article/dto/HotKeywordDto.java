package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotKeywordDto extends BaseDto {

	private int count = 5;
}
