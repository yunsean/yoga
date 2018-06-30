package com.yoga.content.column.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yoga.content.template.model.Template;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cmsColumn")
@Table(name = "cms_column")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "ReturnCmsColumn",
                entities = {@EntityResult(entityClass = Column.class)}
        )
})
public class Column {
    @Id
    private long id;

    @javax.persistence.Column(name = "tenant_id")
    private long tenantId;

    @javax.persistence.Column(name = "name")
    private String name;

    @javax.persistence.Column(name = "parent_id")
    private long parentId;

    @javax.persistence.Column(name = "is_enable")
    private boolean enabled;

    @javax.persistence.Column(name = "template_id")
    private long templateId;

    @javax.persistence.Column(name = "code")
    private String code;

    @Transient
    private List<Column> children = null;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    private Template template;

    public Column() {
        super();
    }

    public Column(long tenantId, String name, long parentId, boolean isEnabled, long templateId, String code) {
        super();
        this.tenantId = tenantId;
        this.name = name;
        this.parentId = parentId;
        this.enabled = isEnabled;
        this.templateId = templateId;
        this.code = code;
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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean isEnable) {
        this.enabled = isEnable;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Column> getChildren() {
        return children;
    }

    public void setChildren(List<Column> children) {
        this.children = children;
    }

    public void addChildren(Column children) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(children);
    }

    public void initChildren() {
        this.children = new ArrayList<>();
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}

