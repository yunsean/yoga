package com.yoga.moment.message.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class MomentUpvoteDto extends BaseDto {

    @ApiModelProperty(value = "消息ID")
    @NotNull(message = "消息ID不能为空")
    private Long messageId;
}
