package com.yoga.jiguang.push;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JiGuangSetting {

    private String appKey;
    private String masterSecret;
    private boolean product = false;
}
