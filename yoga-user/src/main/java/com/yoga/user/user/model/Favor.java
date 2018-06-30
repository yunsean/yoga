package com.yoga.user.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "s_favor")
@JsonInclude(Include.NON_NULL)
public class Favor implements Serializable{
	private static final long serialVersionUID = 3611898658311526077L;

	@Id
	private long id;
	
	@Column(name = "tenant_id")
	private long tenantId;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "object_id")
	private String objectId;
	
	@Column(name = "favor_date")
	private Date favorDate;
	
	@Column(name = "title")	
	private String title;

	@Column(nullable = true, name = "param1")	
	private String param1;

	@Column(nullable = true, name = "param2")	
	private Long param2;

	@Column(nullable = true, name = "param3")
	private String param3;

	@Column(nullable = true, name = "param4")
	private String param4;

	@Column(nullable = true, name = "param5")
	private String param5;
	
	public Favor() {
		super();
	}
	public Favor(long tenantId, long userId, int type, String objectId, Date favorDate, String title, String param1, Long param2, String param3, String param4, String param5) {
		super();
		this.tenantId = tenantId;
		this.userId = userId;
		this.type = type;
		this.objectId = objectId;
		this.favorDate = favorDate;
		this.title = title;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
	}
	public Favor(long tenantId, long userId, int type, long objectId, Date favorDate, String title, String param1, Long param2, String param3, String param4, String param5) {
		super();
		this.tenantId = tenantId;
		this.userId = userId;
		this.type = type;
		this.objectId = String.format("%d", objectId);
		this.favorDate = favorDate;
		this.title = title;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
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
	
	public long getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	public Date getFavorDate() {
		return favorDate;
	}
	public void setFavorDate(Date favorDate) {
		this.favorDate = favorDate;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	
	public Long getParam2() {
		return param2;
	}
	public void setParam2(Long param2) {
		this.param2 = param2;
	}
	
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
}

