package com.yoga.points.summary.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class YearAddDto extends BaseDto {

    @ApiModelProperty(value = "年度年份，比如2020", required = true)
    @NotNull(message = "年度年份不能为空")
    private Integer year;
    @ApiModelProperty(value = "年度开始日期", required = true)
    @NotNull(message = "年度开始日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginDate;
    @ApiModelProperty(value = "年度结束日期", required = true)
    @NotNull(message = "年度结束日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
