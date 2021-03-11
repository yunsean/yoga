package com.yoga.points.summary.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.points.summary.model.PointsYear;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import retrofit2.http.Query;

@Mapper
public interface PointsYearMapper extends MyMapper<PointsYear> {
    PointsYear maxYear(@Param("tenantId") long tenantId);
}
