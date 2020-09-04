package com.yoga.moment.group.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.moment.group.model.MomentGroup;
import com.yoga.moment.group.model.MomentGroupUser;
import com.yoga.moment.message.model.MomentFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MomentGroupMapper extends MyMapper<MomentGroup> {
    List<MomentGroup> listGroup(@Param("userId") long userId);
    List<MomentGroup> list(@Param("tenantId") long tenantId, @Param("filter") String filter);
}
