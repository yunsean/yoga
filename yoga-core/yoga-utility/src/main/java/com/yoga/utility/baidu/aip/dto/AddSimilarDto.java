package com.yoga.utility.baidu.aip.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.ModelAttribute;

@Getter
@Setter
@NoArgsConstructor
public class AddSimilarDto extends BaseDto {
    @ApiModelProperty(value = "图片简介")
    private String brief;
    @ApiModelProperty(value = "图库纬度，也可以用此指定图片桶（一个），后续检索只在这些桶中查找")
    private Long[] tags;
}
