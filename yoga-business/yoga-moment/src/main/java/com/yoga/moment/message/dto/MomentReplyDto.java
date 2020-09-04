package com.yoga.moment.message.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class MomentReplyDto extends BaseDto {

    @ApiModelProperty(value = "消息ID", required = true)
    @NotNull(message = "消息ID不能为空")
    private Long messageId;
    @ApiModelProperty(value = "回复内容", required = true)
    @NotBlank(message = "回复内容不能为空")
    private String content;
    @ApiModelProperty(value = "回复对象，默认为消息创建者")
    private Long receiverId;
}
