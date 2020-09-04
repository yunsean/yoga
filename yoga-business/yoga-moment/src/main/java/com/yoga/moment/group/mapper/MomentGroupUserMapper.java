package com.yoga.moment.group.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.moment.group.ao.UserForGroup;
import com.yoga.moment.group.model.MomentGroup;
import com.yoga.moment.group.model.MomentGroupUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MomentGroupUserMapper extends MyMapper<MomentGroupUser> {
    List<MomentGroupUser> listUser(@Param("groupId") long groupId, @Param("filter") String filter);
    List<UserForGroup> allUser(@Param("tenantId") long tenantId, @Param("groupId") long groupId, @Param("branchId") Long branchId,
                               @Param("dutyId") Long dutyId, @Param("filter") String filter, @Param("includedOnly") boolean includedOnly);
}
