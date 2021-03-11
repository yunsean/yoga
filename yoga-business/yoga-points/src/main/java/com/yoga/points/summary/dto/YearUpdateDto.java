package com.yoga.points.summary.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class YearUpdateDto extends BaseDto {

    @NotNull(message = "年度ID不能为空")
    @ApiModelProperty(value = "年度ID", required = true)
    private Long id;
    @ApiModelProperty(value = "积分年份")
    private Integer year;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "积分年份开始日期")
    private LocalDate beginDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "积分年份结束日期")
    private LocalDate endDate;
}
