package com.yoga.content.article.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SortDto extends TenantDto {

	@NotEmpty(message = "未指定文章ID")
	private String id;
	@NotNull(message = "未指定排序值")
	private Integer sort;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
