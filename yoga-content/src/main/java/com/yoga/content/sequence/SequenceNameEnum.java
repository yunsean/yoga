package com.yoga.content.sequence;

import com.yoga.core.data.BaseEnum;

public enum SequenceNameEnum implements BaseEnum<String> {

	TDD_NO_SEQ("",""),

	SEQ_CMS_COLUMN_ID("seq_cms_column_id", "栏目Id"),	
	SEQ_CMS_TEMPLATE_ID("seq_cms_template_id", "模板Id"),	
	SEQ_CMS_FIELD_ID("seq_cms_field_id", "字段Id"),
	SEQ_CMS_PROPERTY_ID("seq_cms_property_id", "选项Id"),
	SEQ_CMS_LAYOUT_ID("seq_cms_layout_id", "模板Id");

    private final String code;
    private final String desc;
    private SequenceNameEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static SequenceNameEnum getEnum(String code) {
        for (SequenceNameEnum status : SequenceNameEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return desc;
    }
}
