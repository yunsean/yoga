package com.yoga.admin.logging.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LoggingDetailDto extends BaseDto {
    @NotNull(message = "请指定日志ID")
    private Long id;
}
