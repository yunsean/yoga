package com.yoga.ewedding.recharge.model;

import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ew_recharge")
public class Recharge {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "amount")
    private double amount;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "trade_no")
    private String tradeNo;
    @Column(name = "seller_id")
    private String sellerId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RechargeStatus status;
    @Column(name = "refund_time")
    private Date refundTime;
    @Column(name = "refund_actor")
    private long refundActor;
    @Column(name = "expire_to")
    private Date expireTo;
    @Enumerated(EnumType.STRING)
    @Column(name = "recharge_type")
    private RechargeType rechargeType;

    public Recharge() {

    }

    public Recharge(long tenantId, long userId, double amount, String orderNo, Date expireTo, RechargeType rechargeType) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.createTime = new Date();
        this.amount = amount;
        this.orderNo = orderNo;
        this.status = RechargeStatus.pay;
        this.expireTo = expireTo;
        this.rechargeType = rechargeType;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public long getRefundActor() {
        return refundActor;
    }

    public void setRefundActor(long refundActor) {
        this.refundActor = refundActor;
    }

    public Date getExpireTo() {
        return expireTo;
    }

    public void setExpireTo(Date expireTo) {
        this.expireTo = expireTo;
    }

    public RechargeType getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(RechargeType rechargeType) {
        this.rechargeType = rechargeType;
    }
}