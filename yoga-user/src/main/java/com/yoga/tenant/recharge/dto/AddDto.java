package com.yoga.tenant.recharge.dto;

import com.yoga.user.basic.TenantDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class AddDto extends TenantDto {

	@NotNull(message = "租户不能为空")
	private Long tenantId;
	private String refereeA;
	private String refereeB;
	@NotNull(message = "订单金额不能为空")
	private BigDecimal amount;
	@NotNull(message = "过期时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;
	private boolean invoiced;
	private String invoiceNo;
	private String orderNo;
	private String tradeNo;
	private String remark;

	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
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
