package com.yoga.pay.alipay.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pay_water_ali_transfer")
public class AlipayTransferWater {
    @Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "order_id")
    private String orderId;
    @Column(name = "out_biz_no")
    private String outBizNo;
    @Column(name = "pay_date")
    private String payDate;

    public AlipayTransferWater() {
    }

    public AlipayTransferWater(long tenantId, String orderId, String outBizNo, String payDate) {
        this.tenantId = tenantId;
        this.orderId = orderId;
        this.outBizNo = outBizNo;
        this.payDate = payDate;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
