package com.yoga.ewedding.recharge.repo;

import com.yoga.ewedding.recharge.model.Charge;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeRepository extends PagingAndSortingRepository<Charge, Long> {

    Charge findOneByTenantIdAndTypeId(long tenantId, long typeId);
}
