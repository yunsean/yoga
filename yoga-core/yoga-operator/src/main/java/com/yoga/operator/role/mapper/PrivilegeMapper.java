package com.yoga.operator.role.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.role.model.Accredit;
import com.yoga.operator.role.model.Priviege;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivilegeMapper extends MyMapper<Priviege> {
}
