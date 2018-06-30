package com.yoga.imessager.moment.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "im_moment_follow")
public class MomentFollow {
    @Id
    private long id;
    @Column(name = "moment_id")
    private long momentId;
    @Column(name = "creator_id")
    private long creatorId;
    @Column(name = "replier_id")
    private long replierId;
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

    public MomentFollow() {
        super();
    }
    public MomentFollow(long momentId, long creatorId, long replierId, String content, Long receiverId) {
        this.momentId = momentId;
        this.creatorId = creatorId;
        this.replierId = replierId;
        this.replyTime = new Date();
        this.content = content;
        this.receiverId = receiverId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMomentId() {
        return momentId;
    }

    public void setMomentId(long momentId) {
        this.momentId = momentId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getReplierId() {
        return replierId;
    }

    public void setReplierId(long replierId) {
        this.replierId = replierId;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReceiverId() {
        if (receiverId == null || receiverId == 0L) return null;
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getReplier() {
        return replier;
    }

    public void setReplier(String replier) {
        this.replier = replier;
    }

    public String getReplierAvatar() {
        return replierAvatar;
    }

    public void setReplierAvatar(String replierAvatar) {
        this.replierAvatar = replierAvatar;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }
}