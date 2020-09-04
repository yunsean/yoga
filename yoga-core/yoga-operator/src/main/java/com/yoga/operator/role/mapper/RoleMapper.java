package com.yoga.operator.role.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.role.model.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper extends MyMapper<Role> {
}
