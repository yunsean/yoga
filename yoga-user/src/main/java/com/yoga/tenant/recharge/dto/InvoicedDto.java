package com.yoga.tenant.recharge.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class InvoicedDto extends TenantDto {

	@NotNull(message = "续费ID不能为空")
	private Long id;
	@NotNull(message = "发票编号不能为空")
	private String invoiceNo;
	private String orderNo;
	private String tradeNo;
	private String remark;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
