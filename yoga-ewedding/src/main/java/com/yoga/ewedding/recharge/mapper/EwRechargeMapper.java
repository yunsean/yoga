package com.yoga.ewedding.recharge.mapper;

import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.ewedding.recharge.model.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface EwRechargeMapper {
    int countBy(@Param("tenantId") long tenantId, @Param("userId") Long userId, @Param("type") RechargeType type, @Param("status") RechargeStatus status, @Param("user") String user, @Param("begin") Date begin, @Param("end") Date end);
    List<Order> findBy(@Param("tenantId") long tenantId, @Param("userId") Long userId, @Param("type") RechargeType type, @Param("status") RechargeStatus status, @Param("user") String user, @Param("begin") Date begin, @Param("end") Date end, @Param("startIndex") int indexStart, @Param("pageSize") int pageSize);
}
