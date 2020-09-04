package com.yoga.operator.role.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "operator_privilege")
public class Priviege implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tenant_id")
	private Long tenantId;
	@Id
	@Column(name = "role_id")
	private Long roleId;
	@Id
	@Column(name = "code")
	private String code;
}
