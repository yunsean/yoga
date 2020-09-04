package com.yoga.content.column.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_column")
public class Column {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @javax.persistence.Column(name = "tenant_id")
    private Long tenantId;

    @javax.persistence.Column(name = "name")
    private String name;
    @javax.persistence.Column(name = "parent_id")
    private Long parentId;
    @javax.persistence.Column(name = "enabled")
    private Boolean enabled;
    @javax.persistence.Column(name = "template_id")
    private Long templateId;
    @javax.persistence.Column(name = "code")
    private String code;
    @javax.persistence.Column(name = "article_count")
    private Integer articleCount;
    @javax.persistence.Column(name = "remark")
    private String remark;
    @javax.persistence.Column(name = "hidden")
    private Boolean hidden;

    @Transient
    private String templateCode;
    @Transient
    private String templateName;
    @Transient
    private Set<Column> children;
    @Transient
    private Integer childrenCount;

    public Column(Long tenantId, String name, Long parentId, Boolean isEnabled, Long templateId, String code, String remark, boolean hidden) {
        this.tenantId = tenantId;
        this.name = name;
        this.parentId = parentId;
        this.enabled = isEnabled;
        this.templateId = templateId;
        this.code = code;
        this.articleCount = 0;
        this.remark = remark;
        this.hidden = hidden;
    }
    public void addChild(Column child) {
        if (this.children == null) this.children = new HashSet<>();
        this.children.add(child);
    }
}

