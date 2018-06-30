package com.yoga.tenant.sms.repo;

import com.yoga.tenant.sms.model.SmsHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("smsHistoryRepository")
public interface SmsHistoryRepository extends PagingAndSortingRepository<SmsHistory,Long> {
    List<SmsHistory> findByTenantId(long tenantId);
    List<SmsHistory> findAll(Specification<SmsHistory> specification, Pageable pageable);
}
