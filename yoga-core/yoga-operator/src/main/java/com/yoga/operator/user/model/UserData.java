package com.yoga.operator.user.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "operator_user_data")
public class UserData {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "`name`")
	private String name;
	private String value;

	public UserData(Long tenantId, Long userId, String name, String value) {
		this.tenantId = tenantId;
		this.userId = userId;
		this.name = name;
		this.value = value;
	}
}
