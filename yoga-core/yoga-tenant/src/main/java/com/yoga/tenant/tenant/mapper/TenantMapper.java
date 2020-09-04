package com.yoga.tenant.tenant.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.tenant.tenant.model.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends MyMapper<Tenant> {
    void updateSystemTenantId();
}
