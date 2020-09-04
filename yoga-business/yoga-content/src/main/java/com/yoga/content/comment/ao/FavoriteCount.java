package com.yoga.content.comment.ao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteCount {

	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "article_id")
	private String articleId;
	@Column(name = "favorite_count")
	private Integer favoriteCount;
}
