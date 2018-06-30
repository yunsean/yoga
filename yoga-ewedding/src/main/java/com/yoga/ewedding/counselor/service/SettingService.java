package com.yoga.ewedding.counselor.service;

import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.tenant.setting.SettableDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("counselorSettingService")
public class SettingService implements SettableDataSource {
    @Autowired
    private DepartmentService departmentService;

    class DepartmentItem implements SettableDataItem {
        private Department department;
        public DepartmentItem(Department department) {
            this.department = department;
        }
        @Override
        public String getId() {
            return String.valueOf(department.getId());
        }
        @Override
        public String getName() {
            return department.getName();
        }
    }

    @Override
    public List<SettableDataItem> allItems(long tenantId, String module, String key) {
        List<Department> departments = departmentService.childrenOf(tenantId, 0);
        if (departments == null) return null;
        return departments.stream().map(department -> new DepartmentItem(department)).collect(Collectors.toList());
    }
}
