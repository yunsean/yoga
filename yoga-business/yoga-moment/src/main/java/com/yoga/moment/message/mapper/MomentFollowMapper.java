package com.yoga.moment.message.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.model.MomentUpvote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MomentFollowMapper extends MyMapper<MomentFollow> {
    List<MomentFollow> findFollowByMementId(@Param("messageId") long messageId);
}
