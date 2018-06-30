package com.yoga.tenant.recharge.dto;

import com.yoga.user.basic.TenantDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ListDto extends TenantDto {

	private Long tenantId;
	private String tenantName;
	private String referee;
	private String number;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date beginDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	private Boolean invoiced;

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getInvoiced() {
		return invoiced;
	}

	public void setInvoiced(Boolean invoiced) {
		this.invoiced = invoiced;
	}
}
