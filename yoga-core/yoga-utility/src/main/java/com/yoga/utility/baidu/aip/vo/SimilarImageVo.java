package com.yoga.utility.baidu.aip.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("图片结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimilarImageVo {

    @ApiModelProperty(value = "图片描述")
    private String brief;
    @ApiModelProperty(value = "相似度")
    private double score;
}
