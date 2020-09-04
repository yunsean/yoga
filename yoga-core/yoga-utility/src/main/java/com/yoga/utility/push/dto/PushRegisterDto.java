package com.yoga.utility.push.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushRegisterDto extends BaseDto {

    @ApiModelProperty(value = "推送客户端ID", required = true)
    @NotBlank(message = "推送客户端ID不能为空")
    private String clientId;
    @ApiModelProperty("推送渠道[可选]，对于Android，可以指定推送渠道")
    private String pushChannel;
}
