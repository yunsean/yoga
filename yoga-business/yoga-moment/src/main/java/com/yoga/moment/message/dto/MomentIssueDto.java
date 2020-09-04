package com.yoga.moment.message.dto;

import com.yoga.core.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MomentIssueDto extends BaseDto {

    @ApiModelProperty(value = "所属分组，根据业务场景可选")
    private Long groupId;
    @ApiModelProperty(value = "消息内容")
    private String content;
    @ApiModelProperty(value = "消息图片ID")
    private String[] imageIds;
    @ApiModelProperty(value = "消息图片URL")
    private String[] imageUrls;
    @ApiModelProperty(value = "消息附件文件ID")
    private String[] fileIds;
    @ApiModelProperty(value = "消息附件文件名称")
    private String[] fileNames;
    @ApiModelProperty(value = "外联地址")
    private String linkUrl;
    @ApiModelProperty(value = "外联标题")
    private String linkTitle;
    @ApiModelProperty(value = "外联头图")
    private String linkPoster;
    @ApiModelProperty(value = "发送消息时所在位置")
    private String address;
    @ApiModelProperty(value = "发送消息时经度")
    private Double longitude;
    @ApiModelProperty(value = "发送消息时纬度")
    private Double latitude;
    @ApiModelProperty(value = "高德地图POI编码")
    private String poiId;
    @ApiModelProperty(value = "高德地图POI标题")
    private String poiTitle;
}
