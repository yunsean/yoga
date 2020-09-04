package com.yoga.admin.quartz.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuartzTaskVo {

    private String name;
    private String group;
    private String description;
    private String status;
    private String expression;
    private String createTime;
}
