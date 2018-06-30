package com.yoga.imessager.moment.dto;

import com.yoga.user.basic.TenantDto;

public class ListDto extends TenantDto {

    private Long bigThan;
    private Long smallThan;
    private Long deptId;
    private Integer limitCount = 0;
    private boolean wantFollow = false;

    public Long getBigThan() {
        return bigThan;
    }
    public void setBigThan(Long bigThan) {
        this.bigThan = bigThan;
    }

    public Long getSmallThan() {
        return smallThan;
    }
    public void setSmallThan(Long smallThan) {
        this.smallThan = smallThan;
    }

    public Long getDeptId() {
        return deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getLimitCount() {
        return limitCount;
    }
    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public boolean isWantFollow() {
        return wantFollow;
    }
    public void setWantFollow(boolean wantFollow) {
        this.wantFollow = wantFollow;
    }
}
