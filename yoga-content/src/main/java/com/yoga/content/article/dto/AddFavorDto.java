package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class AddFavorDto extends TenantDto {

	@NotEmpty(message = "文章ID不能为空")
	private String articleId;
	private String templateCode;
	@NotEmpty(message = "文章标题不能为空")
	private String articleTitle;
	private String poster;
	private String summary;

	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getArticleTitle() {
		return articleTitle;
	}
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
}
