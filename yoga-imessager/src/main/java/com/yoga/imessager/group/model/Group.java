package com.yoga.imessager.group.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "im_group")
public class Group {

    @Id
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "name")
    private String name;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "member_count")
    private int memberCount;

    @Column(name = "creator_id")
    private long creatorId;

    @Column(name = "creator")
    private String creator;

    public Group() {
    }
    public Group(Long tenantId, String name, String avatar, long creatorId, String creator) {
        this.tenantId = tenantId;
        this.name = name;
        this.avatar = avatar;
        this.memberCount = 0;
        this.createTime = new Date();
        this.creatorId = creatorId;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getMemberCount() {
        return memberCount;
    }
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public long getCreatorId() {
        return creatorId;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
