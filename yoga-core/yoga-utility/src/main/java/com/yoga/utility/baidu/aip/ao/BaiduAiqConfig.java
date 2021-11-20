package com.yoga.utility.baidu.aip.ao;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaiduAiqConfig {
    private String appId;
    private String apiKey;
    private String secretKey;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
