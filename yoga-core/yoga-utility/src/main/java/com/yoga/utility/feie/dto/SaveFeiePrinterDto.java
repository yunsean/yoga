package com.yoga.utility.feie.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class SaveFeiePrinterDto extends BaseDto {
    @NotEmpty(message = "请输入飞鹅打印机编码")
    private String sn;
    @NotEmpty(message = "请输入飞鹅打印机识别码")
    private String key;
    @NotEmpty(message = "请输入飞鹅打印机别名")
    private String name;
}
