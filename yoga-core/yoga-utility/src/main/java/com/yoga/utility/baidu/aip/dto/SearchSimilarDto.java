package com.yoga.utility.baidu.aip.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchSimilarDto extends BaseDto {
    @ApiModelProperty(value = "检索的图片纬度，也可以做桶，将在这些桶中做或操作")
    private Long[] tags;
    @ApiModelProperty(value = "最大返回数量")
    private Integer countLimit = 10;
}
