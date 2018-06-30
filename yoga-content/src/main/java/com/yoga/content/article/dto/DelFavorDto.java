package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;

public class DelFavorDto extends TenantDto {

	private String articleId;
	private Long favorId;

	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public Long getFavorId() {
		return favorId;
	}
	public void setFavorId(Long favorId) {
		this.favorId = favorId;
	}
}
