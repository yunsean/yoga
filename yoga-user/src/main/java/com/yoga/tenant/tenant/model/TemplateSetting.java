package com.yoga.tenant.tenant.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "s_tenant_setting")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateSetting implements Serializable {
	private static final long serialVersionUID = 7440161971721701201L;

	@Id
    private long id;
    @Column(name = "template_id")
    private long templateId;
    @Column(name = "key_name")
    private String key;
    @Column(name = "key_value")
    private String value;
    @Column(name = "module")
    private String module;
    @Column(name = "show_value")
    private String showValue;

    public TemplateSetting() {
        super();
    }


    public TemplateSetting(long id, long templateId, String key, String value, String module, String showValue) {
        this.id = id;
        this.templateId = templateId;
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

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
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
