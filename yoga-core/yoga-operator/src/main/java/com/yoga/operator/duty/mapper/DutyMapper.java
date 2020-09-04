package com.yoga.operator.duty.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.duty.model.Duty;
import com.yoga.operator.role.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DutyMapper extends MyMapper<Duty> {
    void increceLevel(@Param("tenantId") long tenantId, @Param("level") int level);
    List<Duty> list(@Param("tenantId") long tenantId, @Param("filter") String filter);
}
