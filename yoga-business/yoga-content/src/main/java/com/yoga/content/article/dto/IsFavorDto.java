package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class IsFavorDto extends BaseDto {

	@NotEmpty(message = "文章ID不能为空")
	private String articleId;
}
