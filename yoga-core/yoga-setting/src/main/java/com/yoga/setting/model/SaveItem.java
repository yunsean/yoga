package com.yoga.setting.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaveItem {
    private String module;
    private String key;
    private String value;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
