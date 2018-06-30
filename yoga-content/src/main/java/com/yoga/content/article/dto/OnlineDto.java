package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class OnlineDto extends TenantDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;
	@NotNull(message = "未指定是否上线")
	private Boolean online;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
}
