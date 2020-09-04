package com.yoga.moment.message.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moment_upvote")
public class MomentUpvote implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "message_id")
    private long messageId;
    @Id
    @Column(name = "upvoter_id")
    private Long upvoterId;
    @Column(name = "issue_time")
    private Date issueTime;

    @Transient
    private String upvoter;
    @Transient
    private String avater;

    public MomentUpvote(long messageId, Long upvoterId) {
        this.messageId = messageId;
        this.upvoterId = upvoterId;
    }
}