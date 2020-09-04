package com.yoga.operator.role.model;

import lombok.*;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "operator_accredit")
public class Accredit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tenant_id")
	private Long tenantId;
	@Id
	@Column(name = "object_type")
	private Integer objectType;
	@Id
	@Column(name = "object_id")
	private Long objectId;
	@Id
	@Column(name = "role_id")
	private Long roleId;

	public Accredit(long tenantId, int objectType, long objectId, long roleId) {
		this.tenantId = tenantId;
		this.objectType = objectType;
		this.objectId = objectId;
		this.roleId = roleId;
	}
}
