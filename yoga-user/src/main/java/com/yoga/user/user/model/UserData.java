package com.yoga.user.user.model;

import com.alibaba.fastjson.JSON;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "s_user_data")
public class UserData implements Serializable {
	private static final long serialVersionUID = -2518867916017494384L;

	@Id
	private long id;
	@Column(name = "tenant_id")
	private long tenantId;
	@Column(name = "user_id")
	private long userId;
	private String name;
	private String value;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public UserData() {
	}

	public UserData(long id, long tenantId, long userId, String name, String value) {
		this.id = id;
		this.tenantId = tenantId;
		this.userId = userId;
		this.name = name;
		this.value = value;
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
