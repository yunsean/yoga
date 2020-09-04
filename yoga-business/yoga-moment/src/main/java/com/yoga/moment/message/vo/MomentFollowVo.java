package com.yoga.moment.message.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MomentFollowVo {

    private Long id;
    private Long messageId;
    private MomentUserVo replier;
    private MomentUserVo receiver;
    private Date replyTime;
    private String content;
}