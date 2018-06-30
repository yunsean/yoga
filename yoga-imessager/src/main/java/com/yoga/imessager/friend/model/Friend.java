package com.yoga.imessager.friend.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "imFriend")
@Table(name = "im_friend")
@IdClass(FriendPK.class)
public class Friend {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "friend_id")
    private long friendId;

    @Column(name = "tenant_id")
    private long tenantId;

    @Column(name = "add_time")
    private Date addTime;

    @Column(name = "allow_moment")
    private boolean allowMoment;
    @Column(name = "accepted")
    private boolean accepted;
    @Column(name = "blocked")
    private boolean blocked;
    @Column(name = "alias")
    private String alias;

    public Friend() {

    }
    public Friend(long tenantId, long userId, long friendId, boolean allowMoment, String alias) {
        this.userId = userId;
        this.friendId = friendId;
        this.tenantId = tenantId;
        this.addTime = new Date();
        this.allowMoment = allowMoment;
        this.accepted = false;
        this.blocked = false;
        this.alias = alias;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
        return friendId;
    }
    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getTenantId() {
        return tenantId;
    }
    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getAddTime() {
        return addTime;
    }
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public boolean isAllowMoment() {
        return allowMoment;
    }
    public void setAllowMoment(boolean allowMoment) {
        this.allowMoment = allowMoment;
    }

    public boolean isAccepted() {
        return accepted;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
