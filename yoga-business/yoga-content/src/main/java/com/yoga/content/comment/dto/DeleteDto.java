package com.yoga.content.comment.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DeleteDto extends BaseDto {

	@NotNull(message = "ID不能为空")
	private Long id;
}
