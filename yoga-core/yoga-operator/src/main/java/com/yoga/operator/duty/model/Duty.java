package com.yoga.operator.duty.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operator_duty")
public class Duty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	private String name;
	private Integer level;
	private String code;
	private Boolean deleted;
	private String remark;

	@Transient
	private String roles;

	public Duty(long tenantId, String name, int level, String code, String remark) {
		this.tenantId = tenantId;
		this.name = name;
		this.level = level;
		this.code = code;
		this.deleted = false;
		this.remark = remark;
	}
}
