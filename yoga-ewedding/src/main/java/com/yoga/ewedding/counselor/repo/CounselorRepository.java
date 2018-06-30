package com.yoga.ewedding.counselor.repo;

import com.yoga.ewedding.counselor.model.Counselor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselorRepository extends PagingAndSortingRepository<Counselor, Long> {

    Counselor findOneByTenantIdAndProveToken(long tenantId, String proveToken);
}
