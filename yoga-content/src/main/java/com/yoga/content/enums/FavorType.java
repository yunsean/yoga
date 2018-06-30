package com.yoga.content.enums;

import com.yoga.core.data.BaseEnum;

public enum FavorType implements BaseEnum<Integer> {

	Article_Favor(1);
	private final Integer code;
	private FavorType(Integer code) {
		this.code = code;
	}

	@Override
	public Integer getCode() {
		return code;
	}
	@Override
	public String getName() {
		return toString();
	}
}
