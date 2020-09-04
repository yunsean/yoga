package com.yoga.content.comment.mapper;

import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.CommentUpvoteCount;
import com.yoga.content.comment.ao.UpvoteCount;
import com.yoga.content.comment.model.Upvote;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UpvoteMapper extends MyMapper<Upvote> {
    List<UpvoteCount> latestUpvoteCount(@Param("afterTime") LocalDateTime afterTime);
}
