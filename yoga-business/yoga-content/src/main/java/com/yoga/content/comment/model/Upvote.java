package com.yoga.content.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_upvote")
public class Upvote {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "article_id")
	private String articleId;
	@Column(nullable = false, name = "upvoter_id")
	private Long upvoterId;
	@Column(nullable = false, name = "issue_time")
	private LocalDateTime issueTime;

	@Transient
	private String upvoterName;
	@Transient
	private String upvoterAvatar;

	public Upvote(long tenantId, String articleId, Long upvoterId) {
		this.tenantId = tenantId;
		this.articleId = articleId;
		this.upvoterId = upvoterId;
		this.issueTime = LocalDateTime.now();
	}
}
