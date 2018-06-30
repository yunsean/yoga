package com.yoga.content.layout.model;

import com.yoga.content.enums.LayoutType;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;

@Entity
@Table(name = "cms_layout")
public class Layout {
    @Id
    private long Id;
    @Column(name = "tenant_id")
    private long tenantId;
    @Column(name = "template_id")
    private long templateId;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LayoutType type;

    @Column(name = "title")
    private String title;
    @Column(name = "image")
    private String image;
    @Column(name = "fields")
    private String fields;

    @Column(name = "html")
    private String html;
    @Column(name = "css")
    private String css;
    @Column(name = "js")
    private String js;
    @Column(name = "link_files")
    private String linkFiles;

    public Layout() {
    }
    public Layout(long tenantId, long templateId, LayoutType type, String image, String title, String fields, String html, String css, String js, String linkFiles) {
        this.tenantId = tenantId;
        this.templateId = templateId;
        this.type = type;
        this.image = image;
        this.title = title;
        this.fields = fields;
        this.html = html;
        this.css = css;
        this.js = js;
        this.linkFiles = linkFiles;
    }

    public long getId() {
        return Id;
    }
    public void setId(long id) {
        Id = id;
    }

    public long getTenantId() {
        return tenantId;
    }
    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public LayoutType getType() {
        return type;
    }
    public void setType(LayoutType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getFields() {
        return fields;
    }
    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }

    public String getCss() {
        return css;
    }
    public void setCss(String css) {
        this.css = css;
    }

    public String getJs() {
        return js;
    }
    public void setJs(String js) {
        this.js = js;
    }

    public String getLinkFiles() {
        return linkFiles;
    }
    public void setLinkFiles(String linkFiles) {
        this.linkFiles = linkFiles;
    }

    @org.springframework.data.annotation.Transient
    public String[] getFieldList() {
        if (fields == null) return null;
        return fields.split("\\*");
    }

    @Transient
    public String[] getLinkFileList() {
        if (linkFiles == null) return null;
        return linkFiles.split("\\*");
    }
}
