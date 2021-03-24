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
	@ApiModelProperty(value = "是否调整大小")
	private boolean resize = false;
	@ApiModelProperty(value = "宽度，resize=true时有效")
	private Integer width;
	@ApiModelProperty(value = "高度，resize=true时有效")
	private Integer height;
	@ApiModelProperty(value = "上传文件目的，用于统计显示用")
	private String purpose;
	@ApiModelProperty(value = "文件名")
	private String filename;
}
