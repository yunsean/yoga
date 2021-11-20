package com.yoga.utility.baidu.aip.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class SaveSettingDto extends BaseDto {
    @NotBlank(message = "应用ID不能为空")
    private String appId;
    @NotBlank(message = "Api Key不能为空")
    private String apiKey;
    @NotBlank(message = "Secret Key不能为空")
    private String secretKey;
}
