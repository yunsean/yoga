package com.yoga.points.summary.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class SettingDto extends BaseDto {

	@ApiModelProperty(value = "积分年度", required = true)
	@NotNull(message = "当前年度不能为空")
	private Integer annualNum;
	@ApiModelProperty(value = "汇总日期（周几）", required = true)
	@NotNull(message = "汇总日期（周几）不能为空")
	private Integer weekAt;
}
