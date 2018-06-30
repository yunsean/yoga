package com.yoga.user.dept.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "s_department")
public class Department implements Serializable {
	private static final long serialVersionUID = 2834964765196627303L;

	@Id
	private long id;

	@Column(name = "tenant_id")
	private long tenantId;
	
	private String name;
	
	private String remark;
	
	@Column(name = "parent_id")
	private Long parentId;
	
	@Column(name = "create_time")
	private Date createTime;

	private String code;

	@Transient
	private Set<Department> children = null;

	public Department() {

	}
	public Department(long tenantId, String name, String remark, String code) {
		this.tenantId = tenantId;
		this.name = name;
		this.remark = remark;
		this.createTime = new Date();
	}
	public Department(long tenantId, long id, String name) {
		this.id = id;
		this.tenantId = tenantId;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<Department> getChildren() {
		return children;
	}
	public void setChildren(Set<Department> children) {
		this.children = children;
	}
	public void addChild(Department child) {
		if (this.children == null) {
			this.children = new HashSet<>();
		}
		this.children.add(child);
	}
	public void initChildren() {
		this.children = new HashSet<>();
	}
}
