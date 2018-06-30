package com.yoga.core.behavior.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "log_behavior")
public class Behavior {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;

    @Column(name = "actor")
    private String actor;
    @Column(name = "action")
    private String action;

    @Column(name = "date")
    private Date date;
    @Column(name = "operator_id")
    private long operatorId;
    @Column(name = "operator")
    private String operator;

    @Column(name = "remark")
    private String remark;

    public Behavior() {
        super();
    }
    public Behavior(long tenantId, String actor, String action, long operatorId, String operator, String remark) {
        this.tenantId = tenantId;
        this.actor = actor;
        this.action = action;
        this.date = new Date();
        this.operatorId = operatorId;
        this.operator = operator;
        this.remark = remark;
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

    public String getActor() {
        return actor;
    }
    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public long getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
