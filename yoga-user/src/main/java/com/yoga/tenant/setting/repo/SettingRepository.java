package com.yoga.tenant.setting.repo;

import com.yoga.tenant.setting.model.Setting;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends PagingAndSortingRepository<Setting,Long> {
    Setting findOneByTenantIdAndModuleAndKey(long tenantId, String module, String key);
    List<Setting> findByModuleAndKey(String module, String key);
    List<Setting> findByTenantId(long tenantId);
}
