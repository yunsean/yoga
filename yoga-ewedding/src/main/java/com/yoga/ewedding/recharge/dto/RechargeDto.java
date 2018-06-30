package com.yoga.ewedding.recharge.dto;

import com.yoga.ewedding.recharge.enums.PayType;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.user.basic.TenantDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class RechargeDto extends TenantDto{

    @NotNull(message = "请选择充值方式")
    @Enumerated(EnumType.STRING)
    private RechargeType type;
    @NotNull(message = "请选择支付方式")
    @Enumerated(EnumType.STRING)
    private PayType payType;
    private Double amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireTo;

    public RechargeType getType() {
        return type;
    }
    public void setType(RechargeType type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }
    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getExpireTo() {
        return expireTo;
    }
    public void setExpireTo(Date expireTo) {
        this.expireTo = expireTo;
    }
}
