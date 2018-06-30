package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class AllFavorDto extends TenantDto {

	private String title;
	private String filter;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
}
