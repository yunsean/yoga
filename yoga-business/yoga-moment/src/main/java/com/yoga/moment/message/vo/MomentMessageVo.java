package com.yoga.moment.message.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentUpvote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MomentMessageVo {

    private Long id;
    private Long groupId;
    private Long creatorId;
    private String creator;
    private Date issueTime;
    private String content;
    private String linkUrl;
    private String linkTitle;
    private String linkPoster;
    private String address;
    private Double longitude;
    private Double latitude;
    private String poiId;
    private String poiTitle;
    private String branch;
    private String adUrl;
    private String avatar;
    private List<MomentFollowVo> follows;
    private List<MomentUserVo> upvoters;
    private List<String> images;
    private List<MomentFileVo> files;
}