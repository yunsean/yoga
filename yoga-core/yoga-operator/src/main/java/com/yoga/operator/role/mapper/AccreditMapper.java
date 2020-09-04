package com.yoga.operator.role.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.role.model.Accredit;
import com.yoga.operator.role.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface AccreditMapper extends MyMapper<Accredit> {
    Set<String> getPrivileges(@Param("tenantId") long tenantId, @Param("userId") long userId);
}
