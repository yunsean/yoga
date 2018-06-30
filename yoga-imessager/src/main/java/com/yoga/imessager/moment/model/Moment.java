package com.yoga.imessager.moment.model;

import com.yoga.core.utils.StrUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "im_moment")
public class Moment {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "creator_id")
    private long creatorId;
    @Column(name = "issue_time")
    private Date issueTime;
    @Column(name = "content")
    private String content;
    @Column(name = "image_ids")
    private String imageIds;
    @Column(name = "image_urls")
    private String imageUrls;
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
    @Transient
    private String creator;
    @Transient
    private String avatar;
    @Transient
    private List<MomentUpvote> upvotes;
    @Transient
    private List<MomentFollow> follows;

    public Moment() {
        super();
    }
    public Moment(long tenantId, long creatorId, String content, String imageIds, String imageUrls, String linkUrl, String linkTitle, String linkPoster, String address, Double longitude, Double latitude, String poiId, String poiTitle) {
        this.tenantId = tenantId;
        this.creatorId = creatorId;
        this.issueTime = new Date();
        this.content = content;
        this.imageIds = imageIds;
        this.imageUrls = imageUrls;
        this.linkUrl = linkUrl;
        this.linkTitle = linkTitle;
        this.linkPoster = linkPoster;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.poiId = poiId;
        this.poiTitle = poiTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkPoster() {
        return linkPoster;
    }

    public void setLinkPoster(String linkPoster) {
        this.linkPoster = linkPoster;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiTitle() {
        return poiTitle;
    }

    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getCreator() {
        return creator;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<MomentUpvote> getUpvotes() {
        return upvotes;
    }
    public void setUpvotes(List<MomentUpvote> upvotes) {
        this.upvotes = upvotes;
    }

    public List<MomentFollow> getFollows() {
        return follows;
    }
    public void setFollows(List<MomentFollow> follows) {
        this.follows = follows;
    }

    @Transient
    public String[] getImageIdList() {
        return StrUtil.string2Array(imageIds);
    }
    @Transient
    public String[] getImageUrlList() {
        return StrUtil.string2Array(imageUrls);
    }
}