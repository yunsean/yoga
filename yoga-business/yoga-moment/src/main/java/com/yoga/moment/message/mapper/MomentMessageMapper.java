package com.yoga.moment.message.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.model.MomentUpvote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MomentMessageMapper extends MyMapper<MomentMessage> {
    List<MomentMessage> findMomentByTenantId(@Param("tenantId") long tenantId, @Param("groupId") Long groupId, @Param("bigThan") Long bigThan, @Param("smallThan") Long smallThan, @Param("limitCount") Integer limitCount);
    long countNewestMessages(@Param("tenantId") long tenantId, @Param("userId") long userId, @Param("bigThan") Long bigThan);
}
