package com.yoga.ewedding.recharge.mapper;

import com.yoga.ewedding.recharge.model.Charge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChargeMapper {
    List<Charge> findByTenantIdAndParentId(@Param("tenantId") long tenantId, @Param("parentId") long parentId);
}
