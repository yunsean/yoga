package com.yoga.imessager.moment.dto;

import com.yoga.user.basic.TenantDto;

public class IssueDto extends TenantDto {

    private String content;
    private String[] imageIds;
    private String[] imageUrls;
    private String linkUrl;
    private String linkTitle;
    private String linkPoster;
    private String address;
    private Double longitude;
    private Double latitude;
    private String poiId;
    private String poiTitle;

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImageIds() {
        return imageIds;
    }
    public void setImageIds(String[] imageIds) {
        this.imageIds = imageIds;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }
    public void setImageUrls(String[] imageUrls) {
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

    public String getPoiTitle() {
        return poiTitle;
    }
    public void setPoiTitle(String poiTitle) {
        this.poiTitle = poiTitle;
    }

    public String getPoiId() {
        return poiId;
    }
    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }
}
