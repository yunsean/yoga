package com.yoga.content.article.model;

import com.yoga.content.property.model.Property;
import com.yoga.content.template.enums.FieldType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class EditElement {
    private FieldType fieldType;
    private String label;
    private String code;
    private String hint;
    private Object value;
    private Map<String, String> options;
    private List<Property> values;
    private Double longitude;
    private Double latitude;
    private String fileType;
    private String placeholder;
    private List<String> files;
    private List<Document> articles;
    private List<String> checkeds;

    public EditElement(FieldType fieldType, String label, String code, String hint, String placeholder) {
        this.fieldType = fieldType;
        this.label = label;
        this.code = code;
        this.hint = hint;
        this.placeholder = placeholder;
    }
}
