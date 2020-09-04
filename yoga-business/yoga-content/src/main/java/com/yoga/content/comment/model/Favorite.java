package com.yoga.content.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_favorite")
public class Favorite {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "article_id")
	private String articleId;
	@Column(name = "favoriter_id")
	private Long favoriterId;
	@Column(name = "add_time")
	private LocalDateTime addTime;

	public Favorite(Long tenantId, String articleId, Long favoriterId) {
		this.tenantId = tenantId;
		this.favoriterId = favoriterId;
		this.articleId = articleId;
		this.addTime = LocalDateTime.now();
	}
}
