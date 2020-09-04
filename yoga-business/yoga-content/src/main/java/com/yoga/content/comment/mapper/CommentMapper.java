package com.yoga.content.comment.mapper;

import com.yoga.content.comment.ao.CommentCount;
import com.yoga.content.comment.ao.CommentUpvoteCount;
import com.yoga.content.comment.model.Comment;
import com.yoga.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommentMapper extends MyMapper<Comment> {
    List<Comment> list(@Param("tenantId") long tenantId,
                       @Param("articleId") String articleId,
                       @Param("issued") Boolean issued,
                       @Param("replierId") Long replierId,
                       @Param("replyLimit") int replyLimit);
    Comment get(@Param("id") Long id,
                @Param("replyLimit") int replyLimit);
    List<Comment> listReply(@Param("id") Long id);
    CommentUpvoteCount getCount(@Param("articleId") String articleId);
    List<CommentCount> commentCountTop(@Param("tenantId") long tenantId,
                                       @Param("limitCount") Integer limitCount);
    List<CommentCount> latestCommentCount(@Param("afterTime")LocalDateTime afterTime);
    void increaceUpvote(@Param("id") Long id);

    List<Comment> listForAduit(@Param("tenantId") long tenantId,
                               @Param("filter") String filter,
                               @Param("issued") Boolean issued);
}
