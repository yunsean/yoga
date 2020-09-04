package com.yoga.content.template.enums;

import com.yoga.core.base.BaseEnum;

public enum FieldType implements BaseEnum<Integer> {
	TEXT(0, "文本输入"),
	CHOICE(1, "单选输入"),
	IMAGE(2, "图片上传"),
	HTML(3, "网页编辑"),
	GPS(4, "地理定位"),
	DROPDOWN(5, "单选选项"),
    DATE(6, "日期选择"),
	DATETIME(7, "日期时间"),
    ARTICLE(8, "文章关联"),
    DOCUMENT(9, "文档附件"),
    TIME(10, "时间选择"),
    IMAGES(11, "图片列表"),
    SECTION(12, "文字段落"),
	CHECKBOX(13, "多选输入"),
	CHECKDOWN(14, "多选选项"),
	HIDDEN(15, "隐藏参数"),;
    private final Integer code;
    private final String name;
    FieldType(int code, String name) {
        this.code = code;
        this.name = name;
    }

	public Integer getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}