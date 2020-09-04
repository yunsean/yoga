package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OnlineDto extends BaseDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;
	@NotNull(message = "未指定是否上线")
	private Boolean online;
}
