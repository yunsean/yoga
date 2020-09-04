package com.yoga.tenant.tenant.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenantMenuMapper extends MyMapper<TenantMenu> {
    List<TenantMenu> getMenusForUser(@Param("tenantId") long tenantId, @Param("userId") long userId);
}
