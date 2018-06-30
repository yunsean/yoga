package com.yoga.ewedding.recharge.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class SetDto extends TenantDto{

    @NotNull(message = "顾问类型ID不能为空")
    private Long typeId;
    private double originalFee;
    private double monthlyFee;
    private double quarterlyFee;
    private double halfyearFee;
    private double yearlyFee;

    public Long getTypeId() {
        return typeId;
    }
    public void setTypeId(Long typeId) {
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
