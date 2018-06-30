package com.yoga.content.article.model;

import com.mongodb.DBObject;
import com.yoga.content.property.model.Property;
import com.yoga.content.template.enums.FieldType;

import java.util.List;

public class EditElement {
    private FieldType fieldType;
    private String label;
    private String code;
    private String hint;
    private Object value;
    private String[] options;
    private List<Property> values;
    private Double longitude;
    private Double latitude;
    private String fileType;
    private String placeholder;
    private List<DBObject> articles;

    public EditElement(FieldType fieldType, String label, String code, String hint, String placeholder) {
        this.fieldType = fieldType;
        this.label = label;
        this.code = code;
        this.hint = hint;
        this.placeholder = placeholder;
    }

    public FieldType getFieldType() {
        return fieldType;
    }
    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }

    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public String[] getOptions() {
        return options;
    }
    public void setOptions(String[] options) {
        this.options = options;
    }

    public List<Property> getValues() {
        return values;
    }
    public void setValues(List<Property> values) {
        this.values = values;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPlaceholder() {
        return placeholder;
    }
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public List<DBObject> getArticles() {
        return articles;
    }
    public void setArticles(List<DBObject> articles) {
        this.articles = articles;
    }
}
