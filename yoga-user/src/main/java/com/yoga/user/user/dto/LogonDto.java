package com.yoga.user.user.dto;

import com.yoga.core.annotation.Explain;

public class LogonDto extends AddDto {

    @Explain(exclude = true)
    private String phone;
    private String mobile;
    private String uuid;
    private String captcha;
    @Explain(value = "该字段将被忽略", exclude = true)
    private long deptId = 0;
    @Explain(value = "该字段将被忽略", exclude = true)
    private long dutyId = 0;
    @Explain(value = "该字段将被忽略", exclude = true)
    private long[] roleIds;

    @Override
    public String getPhone() {
        return mobile;
    }
    @Override
    public void setPhone(String phone) {
        this.mobile = phone;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public long getDeptId() {
        return deptId;
    }
    @Override
    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    @Override
    public long getDutyId() {
        return 0;
    }
    @Override
    public void setDutyId(long dutyId) {
        this.dutyId = dutyId;
    }

    @Override
    public long[] getRoleIds() {
        return null;
    }
    @Override
    public void setRoleIds(long[] roleIds) {
        this.roleIds = roleIds;
    }
}
