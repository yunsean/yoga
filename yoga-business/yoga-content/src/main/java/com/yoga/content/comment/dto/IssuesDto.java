package com.yoga.content.comment.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IssuesDto {

	@NotEmpty(message = "ID不能为空")
	private List<Long> ids;
	@NotNull(message = "评论发布状态不能为空")
	private Boolean issued;
}
