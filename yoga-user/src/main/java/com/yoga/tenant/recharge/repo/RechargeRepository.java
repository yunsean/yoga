package com.yoga.tenant.recharge.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.tenant.recharge.model.Recharge;
import org.springframework.stereotype.Repository;

@Repository("tenantRechargeRepository")
public interface RechargeRepository extends BaseRepository<Recharge, Long> {
}
