package com.yoga.content.comment.mapper;

import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.CommentUpvoteCount;
import com.yoga.content.comment.model.Comment;
import com.yoga.content.comment.model.CommentUpvote;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommentUpvoterMapper extends MyMapper<CommentUpvote> {
}
