package com.yoga.pay.wxpay.model;

import com.yoga.pay.alipay.enums.WaterType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pay_water_wxpay")
public class WxpayWater {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Enumerated(EnumType.STRING)
    private WaterType type;

    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "trade_no")
    private String tradeNo;
    @Column(name = "bank_type")
    private String bankType;
    @Column(name = "cash_fee")
    private int cashFee;
    @Column(name = "fee_type")
    private String feeType;
    @Column(name = "total_fee")
    private int totalFee;
    @Column(name = "is_subscribe")
    private String isSubscribe;
    @Column(name = "open_id")
    private String openId;
    @Column(name = "time_end")
    private Date timeEnd;
    @Column(name = "trade_type")
    private String tradeType;

    public WxpayWater() {
    }

    public WxpayWater( long tenantId, WaterType type, String orderNo, String tradeNo, String bankType, int cashFee, String feeType, int totalFee, String isSubscribe, String openId, Date timeEnd, String tradeType) {
        this.tenantId = tenantId;
        this.type = type;
        this.orderNo = orderNo;
        this.tradeNo = tradeNo;
        this.bankType = bankType;
        this.cashFee = cashFee;
        this.feeType = feeType;
        this.totalFee = totalFee;
        this.isSubscribe = isSubscribe;
        this.openId = openId;
        this.timeEnd = timeEnd;
        this.tradeType = tradeType;
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

    public WaterType getType() {
        return type;
    }

    public void setType(WaterType type) {
        this.type = type;
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

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }



    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }


    public int getCashFee() {
        return cashFee;
    }

    public void setCashFee(int cashFee) {
        this.cashFee = cashFee;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }
}
