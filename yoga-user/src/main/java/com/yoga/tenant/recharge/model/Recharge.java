package com.yoga.tenant.recharge.model;

import com.yoga.core.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Entity(name = "tenantRecharge")
@Table(name = "g_tenant_recharge")
public class Recharge extends BaseModel {

	@Id
	private long id;
	@Column(name = "tenant_id")
	private long tenantId;
	@Column(name = "tenant_name")
	private String tenantName;
	@Column(name = "operator_id")
	private long operatorId;
	@Column(name = "time")
	private Date time;
	@Column(name = "referee_a")
	private String refereeA;
	@Column(name = "referee_b")
	private String refereeB;
	@Column(name = "amount")
	private BigDecimal amount;
	@Column(name = "expire_date")
	private Date expireDate;
	@Column(name = "invoiced")
	private boolean invoiced;
	@Column(name = "invoice_no")
	private String invoiceNo;
	@Column(name = "order_no")
	private String orderNo;
	@Column(name = "trade_no")
	private String tradeNo;
	@Column(name = "remark")
	private String remark;

	public Recharge() {
		super();
	}
	public Recharge(long tenantId, String tenantName, long operatorId, String refereeA, String refereeB, BigDecimal amount, Date expireDate, boolean invoiced, String invoiceNo, String orderNo, String tradeNo, String remark) {
		this.tenantId = tenantId;
		this.tenantName = tenantName;
		this.operatorId = operatorId;
		this.time = new Date();
		this.refereeA = refereeA;
		this.refereeB = refereeB;
		this.amount = amount;
		this.expireDate = expireDate;
		this.invoiced = invoiced;
		this.invoiceNo = invoiceNo;
		this.orderNo = orderNo;
		this.tradeNo = tradeNo;
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

	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	public String getRefereeA() {
		return refereeA;
	}
	public void setRefereeA(String refereeA) {
		this.refereeA = refereeA;
	}

	public String getRefereeB() {
		return refereeB;
	}
	public void setRefereeB(String refereeB) {
		this.refereeB = refereeB;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public boolean isInvoiced() {
		return invoiced;
	}
	public void setInvoiced(boolean invoiced) {
		this.invoiced = invoiced;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
