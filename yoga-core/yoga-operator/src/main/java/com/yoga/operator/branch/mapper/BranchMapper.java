package com.yoga.operator.branch.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.branch.model.Branch;
import com.yoga.operator.duty.model.Duty;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BranchMapper extends MyMapper<Branch> {
    List<Branch> list(@Param("tenantId") long tenantId);
    List<Branch> childrenOf(@Param("tenantId") long tenantId, @Param("parentId") long parentId, @Param("containSelf") boolean containSelf);
}
