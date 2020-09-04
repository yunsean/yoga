package com.yoga.moment.message.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moment_follow")
public class MomentFollow implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "message_id")
    private Long messageId;
    @Column(name = "creator_id")
    private Long creatorId;
    @Column(name = "replier_id")
    private Long replierId;
    @Column(name = "reply_time")
    private Date replyTime;
    @Column(name = "content")
    private String content;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Transient
    private String replier;
    @Transient
    private String replierAvatar;
    @Transient
    private String receiver;
    @Transient
    private String receiverAvatar;

    public MomentFollow(long messageId, long creatorId, long replierId, String content, Long receiverId) {
        this.messageId = messageId;
        this.creatorId = creatorId;
        this.replierId = replierId;
        this.replyTime = new Date();
        this.content = content;
        this.receiverId = receiverId;
    }
}