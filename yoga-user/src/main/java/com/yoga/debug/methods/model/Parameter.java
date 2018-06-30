package com.yoga.debug.methods.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.core.utils.StrUtil;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter {
    private String name;
    private String type;
    private String explain;
    private List<String> constraints;
    private boolean requestBody;
    private String[] values;
    private String hint;

    public Parameter(String name, String type, String explain, boolean requestBody) {
        this.name = name;
        this.type = type;
        this.explain = StrUtil.isNotBlank(explain) ? explain : null;
        this.requestBody = requestBody;
    }
    public Parameter(String name, String type, String explain, boolean requestBody, List<String> constraints, String[] values, String hint) {
        this.name = name;
        this.type = type;
        this.explain = StrUtil.isNotBlank(explain) ? explain : null;
        this.requestBody = requestBody;
        if (constraints != null) {
            this.constraints = constraints.size() > 0 ? constraints : null;
        }
        this.values = values;
        this.hint = hint;
    }

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getExplain() {
        return explain;
    }
    public List<String> getConstraints() {
        return constraints;
    }
    public boolean isRequestBody() {
        return requestBody;
    }
    public String[] getValues() {
        return values;
    }
    public String getHint() {
        return hint;
    }
}
