package com.yoga.tenant.tenant.service;

public interface TenantChangedListener {
    void onCreated(long tenantId, String name, String code) throws Exception;
    void onModuleChanged(long tenantId, String[] modules) throws Exception;
    void onMenuAdded(long tenantId, String name, String url) throws Exception;
    void onMenuDeleted(long tenantId, String name, String url);
    void onDeleted(long tenantId);
}
