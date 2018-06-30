package com.yoga.user.dept.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.repo.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentCache extends BaseCache {

    @Autowired
    private DepartmentRepository deptRepo;

    @Cacheable(value = "deptsWeb", keyGenerator = "wiselyKeyGenerator")
    public List<Map<Long, String>> getDeptDictionaryWeb(long tenantId){
    	return deptRepo.getAllDept(tenantId);
    }

    @Cacheable(value = "deptsApp", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Department> deptMap(long tenantId) {
        Iterable<Department> departments = deptRepo.findByTenantId(tenantId);
        Map<String, Department> result = new HashMap<>();
        for (Department department : departments) {
            result.put(String.valueOf(department.getId()), department);
        }
        return result;
    }

    @Cacheable(value = "deptsApp", keyGenerator = "wiselyKeyGenerator")
    public List<Department> childrenOf(long tenantId, long parentId) {
        return deptRepo.findByTenantIdAndParentId(tenantId, parentId);
    }

    public void clearDepartmentCache(long tenantId) {
        this.redis.remove("com.yoga.user.dept.cache.DepartmentCache.deptMap." + tenantId);
        this.redis.remove("com.yoga.user.dept.cache.DepartmentCache.getDeptDictionaryWeb." + tenantId);
        this.redis.remove("com.yoga.user.dept.cache.DepartmentCache.childrenOf." + tenantId + ".*");
    }
}
