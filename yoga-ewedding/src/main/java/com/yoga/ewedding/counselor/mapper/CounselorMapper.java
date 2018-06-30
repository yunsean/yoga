package com.yoga.ewedding.counselor.mapper;

import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.ewedding.counselor.model.CounselorUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CounselorMapper {
    int countBy(@Param("tenantId") long tenantId, @Param("deptId")Long deptId, @Param("status")CounselorStatus status, @Param("name") String name, @Param("company") String company);
    List<CounselorUser> findBy(@Param("tenantId") long tenantId, @Param("deptId")Long deptId, @Param("status")CounselorStatus status, @Param("name") String name, @Param("company") String company, @Param("startIndex") int indexStart, @Param("pageSize") int pageSize);
    CounselorUser findByTenantIdAndId(@Param("tenantId") long tenantId, @Param("id") long id);
    List<CounselorUser> findAboveId(@Param("tenantId") long tenantId, @Param("aboveId") Long aboveId);
}
