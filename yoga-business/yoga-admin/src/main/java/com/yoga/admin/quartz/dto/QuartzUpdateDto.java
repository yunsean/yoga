package com.yoga.admin.quartz.dto;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzUpdateDto {

    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "组不能为空")
    private String group;
    @NotBlank(message = "定时方式不能为空")
    private String expression;
    private String description;
}
