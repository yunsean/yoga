package com.yoga.moment.message.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MomentListDto extends BaseDto {

    @ApiModelProperty(value = "只获取大于该值的消息")
    private Long bigThan;
    @ApiModelProperty(value = "只获取小于该值的消息")
    private Long smallThan;
    @ApiModelProperty(value = "消息所属分组过滤")
    private Long groupId;
    @ApiModelProperty(value = "最多消息数量限制")
    private Integer limitCount = 0;
    @ApiModelProperty(value = "是否获取消息回复和点赞列表，默认为false")
    private boolean wantFollow = false;
}
