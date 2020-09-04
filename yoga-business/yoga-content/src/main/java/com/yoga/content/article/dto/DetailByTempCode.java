package com.yoga.content.article.dto;

import com.yoga.core.base.BaseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class DetailByTempCode extends BaseDto {
    @NotNull(message = "模板ID不能为空")
    private String templateCode;
    private String[] fields;
}
