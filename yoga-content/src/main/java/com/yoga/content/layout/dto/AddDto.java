package com.yoga.content.layout.dto;

import com.yoga.content.enums.LayoutType;
import com.yoga.user.basic.TenantDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class AddDto extends TenantDto {

    @NotNull(message = "模板ID不能为空")
    private Long templateId;
    @NotNull(message = "布局类型不能为空")
    @Enumerated(EnumType.STRING)
    private LayoutType type;
    @NotNull(message = "布局标题不能为空")
    private String title;
    private String image;
    private String[] fields;
    @NotNull(message = "布局文件不能为空")
    private String html;
    private String css;
    private String js;
    private String[] accessories;

    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public LayoutType getType() {
        return type;
    }
    public void setType(LayoutType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String[] getFields() {
        return fields;
    }
    public void setFields(String[] fields) {
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

    public String[] getAccessories() {
        return accessories;
    }
    public void setAccessories(String[] accessories) {
        this.accessories = accessories;
    }
}
