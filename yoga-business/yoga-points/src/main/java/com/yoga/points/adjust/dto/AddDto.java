package com.yoga.points.adjust.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AddDto extends BaseDto {

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @ApiModelProperty(value = "调整日期", required = true)
    @NotNull(message = "日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @ApiModelProperty(value = "调整分值（x100）", required = true)
    @NotNull(message = "分值不能为空")
    private Float points;
    @ApiModelProperty(value = "调整理由", required = true)
    @NotEmpty(message = "调整理由不能为空")
    private String reason;
}
