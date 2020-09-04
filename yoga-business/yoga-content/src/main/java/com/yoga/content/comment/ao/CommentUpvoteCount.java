package com.yoga.content.comment.ao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpvoteCount {

	@Column(name = "comment_count")
	private Integer commentCount;
	@Column(name = "upvote_count")
	private Integer upvoteCount;
}
