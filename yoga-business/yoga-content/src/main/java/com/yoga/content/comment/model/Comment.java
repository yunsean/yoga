package com.yoga.content.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_comment")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;
	@Column(name = "article_id")
	private String articleId;
	@Column(name = "article_title")
	private String articleTitle;
	@Column(name = "comment_id")
	private Long commentId;
	@Column(name = "replier_id")
	private Long replierId;
	@Column(name = "replier_name")
	private String replierName;
	@Column(name = "receiver_id")
	private Long receiverId;
	@Column(name = "issue_time")
	private LocalDateTime issueTime;
	@Column(name = "content")
	private String content;
	@Column(name = "upvote_count")
	private Integer upvoteCount;
	@Column(name = "issued")
	private Boolean issued;

	@Transient
	private Integer replyCount;		//评论这条评论的总数
	@Transient
	private List<Long> replyIds;	//评论这条评论的ID列表
	@Transient
	private List<Comment> replies;	//评论这条评论

	public Comment(long tenantId, String articleId, String articleTitle, Long commentId, Long replierId, String replierName, Long receiverId, String content, Boolean issued) {
		this.tenantId = tenantId;
		this.articleId = articleId;
		this.articleTitle = articleTitle;
		this.commentId = commentId;
		this.replierId = replierId;
		this.replierName = replierName;
		this.receiverId = receiverId;
		this.issueTime = LocalDateTime.now();
		this.content = content;
		this.upvoteCount = 0;
		this.issued = issued;
	}

	public Comment(Long id, Boolean issued) {
		this.id = id;
		this.issued = issued;
	}
}
