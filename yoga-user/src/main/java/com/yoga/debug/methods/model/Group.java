package com.yoga.debug.methods.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {
    private String name;
    private String module;
    private List<Method> methods = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        if (StrUtil.isBlank(module)) this.module = null;
        else this.module = module;
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }
    public List<Method> getMethods() {
        return methods;
    }
}
