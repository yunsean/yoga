package com.yoga.moment.message.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentUpvote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MomentUpvoteMapper extends MyMapper<MomentUpvote> {
    List<MomentUpvote> findUpvoteByMementId(@Param("messageId") long messageId);
    long countUpvoteForUser(@Param("userId") long userId, @Param("after")LocalDateTime after);
}
