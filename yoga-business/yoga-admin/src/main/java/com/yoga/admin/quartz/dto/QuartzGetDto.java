package com.yoga.admin.quartz.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuartzGetDto {

    @NotBlank(message = "名称不能为空")
    private String name;
    @NotBlank(message = "组不能为空")
    private String group;
}
