package com.yoga.content.comment.ao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class CommentCount {

	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "article_id")
	private String articleId;
	@Column(name = "comment_count")
	private Integer commentCount;
}
