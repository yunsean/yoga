package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class DeleteDto extends TenantDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
