package com.yoga.admin.aggregate.ao;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.SeriesType;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Statement implements Serializable {
    private static final long serialVersionUID = 1L;
    private String module;
    private String name;
    private String json;

    public Statement(String module, String name, Option option) {
        this.module = module;
        this.name = name;
        this.json = JSON.toJSONString(option);
    }
}
