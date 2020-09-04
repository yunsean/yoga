package com.yoga.admin.aggregate.ao;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiParam("所属模块")
    private String module;
    @ApiParam("图标，从materialdesignicons中查询")
    private String icon;
    @ApiParam("标题，重复标题可以合并")
    private String title;
    @ApiParam("内容")
    private String content;
    @ApiParam("动作，通过动作确定如何跳转")
    private String action;
    @ApiParam("是否紧急任务")
    private boolean urgency;
    @ApiParam("动作需要的权限")
    private String permission;
    @ApiParam("跳转时的参数，前后台约定")
    private String param;

    public Todo(String module, String icon, String title, String content, String action, boolean urgency, String permission) {
        this.module = module;
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.action = action;
        this.urgency = urgency;
        this.permission = permission;
    }
    public Todo(String module, String icon, String title, String content, String action, boolean urgency, String permission, Long param) {
        this.module = module;
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.action = action;
        this.urgency = urgency;
        this.permission = permission;
        this.param = String.valueOf(param);
    }
    public Todo(String module, String icon, String title, String content, String action, boolean urgency, String permission, String param) {
        this.module = module;
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.action = action;
        this.urgency = urgency;
        this.permission = permission;
        this.param = param;
    }
}
