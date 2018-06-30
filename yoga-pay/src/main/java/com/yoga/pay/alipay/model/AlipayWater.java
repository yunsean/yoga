package com.yoga.pay.alipay.model;

import com.yoga.pay.alipay.enums.WaterType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pay_water_alipay")
public class AlipayWater {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private WaterType type;
    @Column(name = "return_time")
    private Date returnTime;
    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "trade_no")
    private String tradeNo;
    @Column(name = "buyer")
    private String buyer;
    @Column(name = "status")
    private String status;
    @Column(name = "response")
    private String response;
    @Column(name = "rsa_checked")
    private boolean rsaChecked;
    @Column(name = "biz_result")
    private String bizResult;

    public AlipayWater(){
    }
    public AlipayWater(long tenantId, WaterType type, String orderNo, String tradeNo, String buyer, String status, String response, boolean rsaChecked, String bizResult) {
        this.tenantId = tenantId;
        this.type = type;
        this.returnTime = new Date();
        this.orderNo = orderNo;
        this.tradeNo = tradeNo;
        this.buyer = buyer;
        this.status = status;
        this.response = response;
        this.rsaChecked = rsaChecked;
        this.bizResult = bizResult;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WaterType getType() {
        return type;
    }

    public void setType(WaterType type) {
        this.type = type;
    }

    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
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

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isRsaChecked() {
        return rsaChecked;
    }

    public void setRsaChecked(boolean rsaChecked) {
        this.rsaChecked = rsaChecked;
    }

    public String getBizResult() {
        return bizResult;
    }

    public void setBizResult(String bizResult) {
        this.bizResult = bizResult;
    }
}