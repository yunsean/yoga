package com.yoga.utility.journal.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "u_journal")
public class Journal {

    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "time")
    private Date time;
    @Column(name = "module")
    private String module;
    @Column(name = "method")
    private String method;
    @Column(name = "action")
    private String action;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "user")
    private String user;
    @Column(name = "detail")
    private String detail;

    public Journal() {
    }
    public Journal(long tenantId, String module, String method, String action, long userId, String user, String detail) {
        this.tenantId = tenantId;
        this.time = new Date();
        this.module = module;
        this.method = method;
        this.action = action;
        this.userId = userId;
        this.user = user;
        this.detail = detail;
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

    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
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

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
