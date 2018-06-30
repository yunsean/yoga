package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class WebListDto extends TenantDto {

	private String articleId;
	private Long columnId;
	private String columnCode;
	private String name;
	private boolean alone = false;		//是否独立页面（左侧没有栏目树)

	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getColumnCode() {
		return columnCode;
	}
	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isAlone() {
		return alone;
	}
	public void setAlone(boolean alone) {
		this.alone = alone;
	}
}
