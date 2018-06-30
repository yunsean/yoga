package com.yoga.imessager.group.model;

import com.yoga.imessager.group.enums.UserType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GroupUserPK.class)
@Table(name = "im_group_user")
public class GroupUser implements Serializable {

    @Id
    @Column(name = "group_id")
    private Long groupId = null;

    @Id
    @Column(name = "user_id")
    private Long userId = null;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "applying")
    private boolean applying;

    public GroupUser() {
    }
    public GroupUser(Long groupId, Long userId, UserType userType) {
        this.groupId = groupId;
        this.userId = userId;
        this.userType = userType;
        this.applying = false;
    }

    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserType getUserType() {
        return userType;
    }
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isApplying() {
        return applying;
    }
    public void setApplying(boolean applying) {
        this.applying = applying;
    }
}

