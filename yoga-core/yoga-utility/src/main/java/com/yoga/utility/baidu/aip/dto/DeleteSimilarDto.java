package com.yoga.utility.baidu.aip.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteSimilarDto extends BaseDto {
    @ApiModelProperty(value = "图库纬度，也可以用此指定图片桶（一个），后续检索只在这些桶中查找")
    private Long[] tags;
}
