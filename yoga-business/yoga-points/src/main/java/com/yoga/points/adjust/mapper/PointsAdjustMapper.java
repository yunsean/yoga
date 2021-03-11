package com.yoga.points.adjust.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.points.adjust.ao.SummaryItem;
import com.yoga.points.adjust.model.PointsAdjust;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PointsAdjustMapper extends MyMapper<PointsAdjust> {
    List<PointsAdjust> list(@Param("tenantId") long tenantId,
                            @Param("userId") Long userId,
                            @Param("branchId") Long branchId,
                            @Param("dutyId") Long dutyId,
                            @Param("keyword") String keyword,
                            @Param("beginDate") LocalDate beginDate,
                            @Param("endDate") LocalDate endDate);
    List<SummaryItem> penaltyPoints(@Param("tenantId") long tenantId,
                                    @Param("beginDate") LocalDate beginDate,
                                    @Param("endDate") LocalDate endDate);
    List<SummaryItem> rewardPoints(@Param("tenantId") long tenantId,
                                   @Param("beginDate") LocalDate beginDate,
                                   @Param("endDate") LocalDate endDate);
}
