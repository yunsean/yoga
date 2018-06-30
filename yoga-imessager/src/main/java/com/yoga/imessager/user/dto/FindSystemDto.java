package com.yoga.imessager.user.dto;

import com.yoga.core.annotation.Explain;
import com.yoga.user.basic.TenantDto;

public class FindSystemDto extends TenantDto {

    @Explain("不在群组ID")
    private long groupId;
    @Explain("全名")
    private String nickname;
    @Explain("部门ID")
    private long deptId = 0;

    public long getGroupId() {
        return groupId;
    }
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getDeptId() {
        return deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
