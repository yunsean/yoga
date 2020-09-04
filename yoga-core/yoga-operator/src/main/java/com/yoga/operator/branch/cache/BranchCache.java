package com.yoga.operator.branch.cache;

import com.yoga.core.base.BaseCache;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.operator.branch.mapper.BranchMapper;
import com.yoga.operator.branch.model.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BranchCache extends BaseCache {

    @Autowired
    private BranchMapper branchMapper;

    @Cacheable(value = "branch", keyGenerator = "wiselyKeyGenerator")
    public Map<String, String> nameMap(long tenantId){
    	List<Branch> branches = new MapperQuery<>(Branch.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(branchMapper);
    	return branches.stream().collect(Collectors.toMap(b-> b.getId().toString(), Branch::getName));
    }
    @Cacheable(value = "branch", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Branch> map(long tenantId) {
        List<Branch> branches = new MapperQuery<>(Branch.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(branchMapper);
        return branches.stream().collect(Collectors.toMap(b-> b.getId().toString(), b-> b));
    }
    @Cacheable(value = "branch", keyGenerator = "wiselyKeyGenerator")
    public List<Branch> list(long tenantId) {
        return new MapperQuery<>(Branch.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(branchMapper);
    }

    @Cacheable(value = "branch", keyGenerator = "wiselyKeyGenerator")
    public List<Branch> childrenOf(long tenantId, long parentId, boolean containSelf) {
        return branchMapper.childrenOf(tenantId, parentId, containSelf);
    }

    public void clearCache(long tenantId) {
        this.redisOperator.remove("branch::nameMap:" + tenantId);
        this.redisOperator.remove("branch::map:" + tenantId);
        this.redisOperator.remove("branch::list:" + tenantId);
        this.redisOperator.removePattern("branch::childrenOf:" + tenantId + ".*");
    }
}
