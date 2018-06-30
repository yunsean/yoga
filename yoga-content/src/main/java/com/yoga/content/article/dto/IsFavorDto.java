package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class IsFavorDto extends TenantDto {

	@NotEmpty(message = "文章ID不能为空")
	private String articleId;

	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
}
