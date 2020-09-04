package com.yoga.moment.message.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "moment_message")
public class MomentMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "creator_id")
    private Long creatorId;
    @Column(name = "issue_time")
    private Date issueTime;
    @Column(name = "content")
    private String content;
    @Column(name = "image_ids")
    private String imageIds;
    @Column(name = "image_urls")
    private String imageUrls;
    @Column(name = "file_ids")
    private String fileIds;
    @Column(name = "file_names")
    private String fileNames;
    @Column(name = "link_url")
    private String linkUrl;
    @Column(name = "link_title")
    private String linkTitle;
    @Column(name = "link_poster")
    private String linkPoster;
    @Column(name = "address")
    private String address;
    @Column(name = "longitude")
    private Double longitude;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "poi_id")
    private String poiId;
    @Column(name = "poi_title")
    private String poiTitle;
    @Column(name = "ad_url")
    private String adUrl;
    @Column(name = "updated_time")
    private Date updatedTime;

    @Transient
    private String creator;
    @Transient
    private String avatar;
    @Transient
    private String branch;
    @Transient
    private List<MomentUpvote> upvotes;
    @Transient
    private List<MomentFollow> follows;

    public MomentMessage(long tenantId, long creatorId, Long groupId, String content, String imageIds, String imageUrls, String fileIds, String fileNames, String linkUrl, String linkTitle, String linkPoster, String address, Double longitude, Double latitude, String poiId, String poiTitle) {
        this.tenantId = tenantId;
        this.groupId = groupId;
        this.creatorId = creatorId;
        this.issueTime = new Date();
        this.content = content;
        this.imageIds = imageIds;
        this.imageUrls = imageUrls;
        this.fileIds = fileIds;
        this.fileNames = fileNames;
        this.linkUrl = linkUrl;
        this.linkTitle = linkTitle;
        this.linkPoster = linkPoster;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.poiId = poiId;
        this.poiTitle = poiTitle;
        this.updatedTime = new Date();
    }
}