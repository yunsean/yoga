package com.yoga.setting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_setting")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Setting implements Serializable {
	private static final long serialVersionUID = 7440161971721701201L;

	@Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Column(name = "tenant_id")
    private Long tenantId;
    @Column(name = "module")
    private String module;
    @Column(name = "`key`")
    private String key;
    @Column(name = "`value`")
    private String value;
    @Column(name = "show_value")
    private String showValue;

    public Setting(long tenantId, String module, String key, String value, String showValue) {
        this.tenantId = tenantId;
        this.module = module;
        this.key = key;
        this.value = value;
        this.showValue = showValue;
    }
}
