package com.yoga.tenant.sms.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sms_history")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsHistory implements Serializable {

	@Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "date")
    private Date date;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "action")
    private String action;

    public SmsHistory() {

    }
    public SmsHistory(long tenantId, Date date, String mobile, String action) {
        this.tenantId = tenantId;
        this.date = date;
        this.mobile = mobile;
        this.action = action;
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

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
}
