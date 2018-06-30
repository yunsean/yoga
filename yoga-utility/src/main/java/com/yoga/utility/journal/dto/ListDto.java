package com.yoga.utility.journal.dto;

import com.yoga.user.basic.TenantDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ListDto extends TenantDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date beginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    private String user;
    private String module;
    private String method;
    private String action;

    public Date getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getModule() {
        return module;
    }
    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}
