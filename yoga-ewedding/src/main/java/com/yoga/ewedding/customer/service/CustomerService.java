package com.yoga.ewedding.customer.service;


import com.yoga.core.service.BaseService;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("eweddingCustomerService")
public class CustomerService extends BaseService {

    @Autowired
    private SettingService settingService;

    public final static String Module_Name = "ew_customer";
    public final static String DepartmentId_Key = "customer.department.id";
        public Long getDepartmentId(long tenantId) {
        Number id = settingService.get(tenantId, Module_Name, DepartmentId_Key, Long.class);
        if (id == null) return null;
        return id.longValue();
    }
}
