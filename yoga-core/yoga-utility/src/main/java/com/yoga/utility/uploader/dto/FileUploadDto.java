package com.yoga.utility.uploader.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadDto extends BaseDto {
	@ApiModelProperty("是否调整大小")
	private boolean resize = false;
	@ApiModelProperty("宽度，resize=true时有效")
	private Integer width;
	@ApiModelProperty("高度，resize=true时有效")
	private Integer height;
	@ApiModelProperty("上传文件目的，用于统计显示用")
	private String purpose;
}
