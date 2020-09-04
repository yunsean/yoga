package com.yoga.operator.role.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operator_role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	private String name;
	private String remark;
	private Boolean deleted;
	@Column(name = "create_time")
	private Date createTime;

	public Role(long tenantId, String name, String remark) {
		this.tenantId = tenantId;
		this.name = name;
		this.remark = remark;
		this.deleted = false;
		this.createTime = new Date();
	}
}
