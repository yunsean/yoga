package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class HotKeywordDto extends TenantDto {

	private int count = 5;

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
