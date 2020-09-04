package com.yoga.content.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cms_comment_upvote")
public class CommentUpvote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "comment_id")
	private Long commentId;
	@Column(name = "upvoter_id")
	private Long upvoterId;
	@Column(name = "add_time")
	private LocalDateTime addTime;

	public CommentUpvote(Long commentId, Long upvoterId) {
		this.commentId = commentId;
		this.upvoterId = upvoterId;
	}
}
