package com.yoga.content.template.enums;

import com.yoga.core.data.BaseEnum;

public enum FieldType implements BaseEnum<Integer> {
	TEXT(0, "文本输入"),
	CHOICE(1, "单选输入"),
	IMAGE(2, "图片上传"),
	HTML(3, "网页编辑"),
	GPS(4, "地理定位"),
	DROPDOWN(5, "下拉选择"),
    DATE(6, "日期选择"),
	DATETIME(7, "日期时间"),
    ARTICLE(8, "文章关联"),
    DOCUMENT(9, "文档附件"),
    TIME(10, "时间选择"),
    IMAGES(11, "图片列表"),
    SECTION(12, "文字段落");
    private final Integer code;
    private final String name;
    FieldType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FieldType getEnum(int code) {
        for (FieldType status : FieldType.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
	public Integer getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}