package com.yoga.utility.uploader.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageFileVo {

	@ApiModelProperty("图片ID")
	private Long id;
	@ApiModelProperty("图片路径")
	private String remoteUrl;
}
