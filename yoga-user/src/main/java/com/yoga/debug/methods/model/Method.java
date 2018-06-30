package com.yoga.debug.methods.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.core.utils.StrUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Method {
    private String explain;
    private String url;
    private String module;
    private List<Parameter> parameters;

    public Method(String url, String explain, String module) {
        this.url = url;
        this.explain = StrUtil.isNotBlank(explain) ? explain : null;
        this.module = StrUtil.isNotBlank(module) ? module : null;
    }

    public String getUrl() {
        return url;
    }
    public String getExplain() {
        return explain;
    }
    public String getModule() {
        return module;
    }
    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
