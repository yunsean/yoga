package com.yoga.ewedding.recharge.repo;

import com.yoga.ewedding.recharge.enums.RechargeStatus;
import com.yoga.ewedding.recharge.model.Recharge;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRepository extends PagingAndSortingRepository<Recharge, Long> {

    List<Recharge> findByTenantIdAndUserIdAndStatus(long tenantId, long userId, RechargeStatus status);
    Recharge findByOrderNo(String orderNo);
}
