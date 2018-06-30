package com.yoga.content.layout.dto;

import com.yoga.user.basic.TenantDto;

import javax.validation.constraints.NotNull;

public class UpdateDto extends TenantDto {
    @NotNull(message = "布局ID不能为空")
    private Long id;
    private String title;
    private String image;
    private String[] fields;
    private String html;
    private String css;
    private String js;
    private String[] accessories;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
