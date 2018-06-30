package com.yoga.ewedding.recharge.dto;

import com.yoga.ewedding.recharge.enums.PayType;
import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.user.basic.TenantDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class RechargeListDto extends TenantDto{

    @Enumerated(EnumType.STRING)
    private RechargeType type;
    @Enumerated(EnumType.STRING)
    private RechargeStatus status;
    private String user;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    public RechargeType getType() {
        return type;
    }
    public void setType(RechargeType type) {
        this.type = type;
    }

    public RechargeStatus getStatus() {
        return status;
    }
    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public Date getBegin() {
        return begin;
    }
    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
    }
}
