package com.yoga.points.summary.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.points.summary.model.PointsSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointsSummaryMapper extends MyMapper<PointsSummary> {
    List<PointsSummary> list(@Param("tenantId") long tenantId,
                             @Param("year") int year,
                             @Param("userId") Long userId,
                             @Param("branchId") Long branchId,
                             @Param("dutyId") Long duty,
                             @Param("keyword") String keyword,
                             @Param("penaltyOnly") Boolean penalty,
                             @Param("orderBy") String orderBy);
    List<Long> lowestPoints(@Param("tenantId") long tenantId,
                            @Param("year") int year,
                            @Param("count") long count);
}
