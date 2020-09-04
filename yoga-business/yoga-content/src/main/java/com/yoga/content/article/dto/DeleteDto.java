package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class DeleteDto extends BaseDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;
}
