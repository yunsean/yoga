package com.yoga.tenant.tenant.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TenantChangedObserver {
    private List<TenantChangedListener> changedListeners = new ArrayList<>();

    public void register(TenantChangedListener entry) {
        synchronized (TenantChangedObserver.class) {
            changedListeners.add(entry);
        }
    }
    public void unregister(TenantChangedListener entry) {
        synchronized (TenantChangedObserver.class) {
            changedListeners.remove(entry);
        }
    }

    public void onCreated(long tenantId, String name, String code) throws Exception {
        for (TenantChangedListener listener : changedListeners) {
            listener.onCreated(tenantId, name, code);
        }
    }
    public void onModuleChanged(long tenantId, String[] modules) throws Exception {
        for (TenantChangedListener listener : changedListeners) {
            listener.onModuleChanged(tenantId, modules);
        }
    }
    public void onMenuAdded(long tenantId, String name, String url) throws Exception {
        for (TenantChangedListener listener : changedListeners) {
            listener.onMenuAdded(tenantId, name, url);
        }
    }
    public void onMenuDeleted(long tenantId, String name, String url) {
        for (TenantChangedListener listener : changedListeners) {
            listener.onMenuDeleted(tenantId, name, url);
        }
    }
    public void onDeleted(long tenantId) {
        for (TenantChangedListener listener : changedListeners) {
            listener.onDeleted(tenantId);
        }
    }
}
