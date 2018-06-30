package com.yoga.tenant.setting.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "s_setting")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Setting implements Serializable {
	private static final long serialVersionUID = 7440161971721701201L;

	@Id
    private long id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "key_name")
    private String key;
    @Column(name = "key_value")
    private String value;
    @Column(name = "module")
    private String module;
    @Column(name = "show_value")
    private String showValue;

    public Setting() {
        super();
    }


    public Setting(long id, long tenantId, String key, String value, String module, String showValue) {
        this.id = id;
        this.tenantId = tenantId;
        this.key = key;
        this.value = value;
        this.module = module;
        this.showValue = showValue;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getShowValue() {
        return showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }
}
