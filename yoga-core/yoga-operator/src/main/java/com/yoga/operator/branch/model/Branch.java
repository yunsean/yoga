package com.yoga.operator.branch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operator_branch")
public class Branch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	private String name;
	private String remark;
	@Column(name = "parent_id")
	private Long parentId;
	@Column(name = "create_time")
	private Date createTime;
	private Boolean deleted;

	@Transient
	private String roles;
	@Transient
	private Set<Branch> children;

	public Branch(long tenantId, String name, String remark, Long parentId) {
		this.tenantId = tenantId;
		this.name = name;
		this.remark = remark;
		this.parentId = parentId;
		this.createTime = new Date();
		this.deleted = false;
	}

	public void addChild(Branch child) {
		if (this.children == null) this.children = new HashSet<>();
		this.children.add(child);
	}
}
