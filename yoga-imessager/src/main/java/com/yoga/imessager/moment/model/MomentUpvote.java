package com.yoga.imessager.moment.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(MomentUpvotePK.class)
@Table(name = "im_moment_upvote")
public class MomentUpvote {
    @Id
    @Column(name = "moment_id")
    private long momentId;
    @Id
    @Column(name = "upvoter_id")
    private Long upvoterId;
    @Column(name = "issue_time")
    private Date issueTime;

    @Transient
    private String upvoter;
    @Transient
    private String upvoterAvater;

    public MomentUpvote() {
        super();
    }
    public MomentUpvote(long momentId, Long upvoterId) {
        this.momentId = momentId;
        this.upvoterId = upvoterId;
        this.issueTime = new Date();
    }

    public long getMomentId() {
        return momentId;
    }
    public void setMomentId(long momentId) {
        this.momentId = momentId;
    }

    public Long getUpvoterId() {
        return upvoterId;
    }
    public void setUpvoterId(Long upvoterId) {
        this.upvoterId = upvoterId;
    }

    public Date getIssueTime() {
        return issueTime;
    }
    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getUpvoter() {
        return upvoter;
    }
    public void setUpvoter(String upvoter) {
        this.upvoter = upvoter;
    }

    public String getUpvoterAvater() {
        return upvoterAvater;
    }
    public void setUpvoterAvater(String upvoterAvater) {
        this.upvoterAvater = upvoterAvater;
    }
}