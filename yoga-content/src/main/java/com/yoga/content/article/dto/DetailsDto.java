package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class DetailsDto extends TenantDto {

	@NotEmpty(message = "未指定文章ID")
	private String[] articleIds;

	public String[] getArticleIds() {
		return articleIds;
	}
	public void setArticleIds(String[] articleIds) {
		this.articleIds = articleIds;
	}
}
