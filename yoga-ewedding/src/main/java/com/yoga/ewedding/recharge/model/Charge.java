package com.yoga.ewedding.recharge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ew_charge")
public class Charge {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "type_id")
    private long typeId;
    @Column(name = "original_fee")
    private double originalFee;
    @Column(name = "monthly_fee")
    private double monthlyFee;
    @Column(name = "halfyear_fee")
    private double halfyearFee;
    @Column(name = "quarterly_fee")
    private double quarterlyFee;
    @Column(name = "yearly_fee")
    private double yearlyFee;

    public Charge() {
    }
    public Charge(long tenantId, long typeId, double originalFee, double monthlyFee, double quarterlyFee, double halfyearFee, double yearlyFee) {
        this.tenantId = tenantId;
        this.typeId = typeId;
        this.originalFee = originalFee;
        this.monthlyFee = monthlyFee;
        this.quarterlyFee = quarterlyFee;
        this.halfyearFee = halfyearFee;
        this.yearlyFee = yearlyFee;
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

    public long getTypeId() {
        return typeId;
    }
    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public double getOriginalFee() {
        return originalFee;
    }
    public void setOriginalFee(double originalFee) {
        this.originalFee = originalFee;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }
    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public double getQuarterlyFee() {
        return quarterlyFee;
    }
    public void setQuarterlyFee(double quarterlyFee) {
        this.quarterlyFee = quarterlyFee;
    }

    public double getHalfyearFee() {
        return halfyearFee;
    }
    public void setHalfyearFee(double halfyearFee) {
        this.halfyearFee = halfyearFee;
    }

    public double getYearlyFee() {
        return yearlyFee;
    }
    public void setYearlyFee(double yearlyFee) {
        this.yearlyFee = yearlyFee;
    }
}