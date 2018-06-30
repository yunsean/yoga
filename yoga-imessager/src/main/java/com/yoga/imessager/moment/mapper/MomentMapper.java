package com.yoga.imessager.moment.mapper;

import com.yoga.imessager.moment.model.Moment;
import com.yoga.imessager.moment.model.MomentFollow;
import com.yoga.imessager.moment.model.MomentUpvote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MomentMapper {
    List<Moment> findMomentByTenantId(@Param("tenantId")long tenantId, @Param("deptId")Long deptId, @Param("bigThan")Long bigThan, @Param("smallThan")Long smallThan, @Param("limitCount")Integer limitCount);
    List<MomentFollow> findFollowByMementId(@Param("momentId")long momentId);
    List<MomentUpvote> findUpvoteByMementId(@Param("momentId")long momentId);
}
