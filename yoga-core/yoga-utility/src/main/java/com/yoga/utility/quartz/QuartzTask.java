package com.yoga.utility.quartz;

import lombok.*;
import org.quartz.JobDataMap;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzTask {

    private JobDataMap dataMap;
    private String name;
    private String group;
    private String description;
    private String status;
    private String expression;
    private String createTime;

    public QuartzTask(String name, String group, String description, String expression) {
        this.name = name;
        this.group = group;
        this.description = description;
        this.expression = expression;
    }
    public QuartzTask(Class<?> clazz, String group, String description, String expression) {
        this.name = clazz.getName();
        this.group = group;
        this.description = description;
        this.expression = expression;
    }

    public String jobClass() {
        return name == null ? null : name.split("#")[0];
    }
}
