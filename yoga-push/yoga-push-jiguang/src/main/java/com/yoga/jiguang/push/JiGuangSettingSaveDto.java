package com.yoga.jiguang.push;

import com.yoga.core.base.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JiGuangSettingSaveDto extends BaseDto {

    private String appKey;
    private String masterSecret;
    private boolean product;
}
