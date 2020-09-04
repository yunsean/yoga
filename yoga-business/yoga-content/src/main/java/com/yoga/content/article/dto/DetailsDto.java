package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetailsDto extends BaseDto {

	@NotEmpty(message = "未指定文章ID")
	private List<String> articleIds;
	private String[] fields;
}
