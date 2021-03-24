package com.yoga.operator.branch.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.operator.branch.cache.BranchCache;
import com.yoga.operator.branch.mapper.BranchMapper;
import com.yoga.operator.branch.model.Branch;
import com.yoga.operator.role.service.PrivilegeService;
import com.yoga.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@LoggingPrimary(module = BranchService.ModuleName, name = "部门管理")
public class BranchService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private BranchCache branchCache;

    public final static String ModuleName = "admin_branch";
    public final static String Key_LogonBranch = "branch.logon.id";
    public final static String Key_MaxLevel = "branch.max.level";

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Branch branch = branchMapper.selectByPrimaryKey(primaryId);
        if (branch == null) return null;
        return branch.getName();
    }

    @Transactional
    @Logging(module = ModuleName, description = "添加部门", primaryKeyIndex = -1, argNames = "租户ID，父级ID，名称，备注，角色ID")
    public long add(long tenantId, long parentId, String name, String remark, Long[] roleIds) {
        if (new MapperQuery<>(Branch.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("parentId", parentId)
                .andEqualTo("name", name)
                .andEqualTo("deleted", false)
                .count(branchMapper) > 0) throw new BusinessException("已存在同名的部门");
        if (parentId != 0) {
            Branch parent = branchMapper.selectByPrimaryKey(parentId);
            if (parent == null) throw new BusinessException("父级部门不存在！");
            int maxLevel = getMaxLevel(tenantId);
            if (maxLevel > 0) {
                List<Branch> parents = branchMapper.parentOf(tenantId, parentId, true);
                if (parents.size() >= maxLevel) throw new BusinessException("最多只允许创建" + maxLevel + "级部门！");
            }
        }

        Branch branch = new Branch(tenantId, name, remark, parentId);
        branchMapper.insert(branch);
        privilegeService.setBranchRoles(tenantId, branch.getId(), roleIds);
        branchCache.clearCache(tenantId);
        return branch.getId();
    }
    @Transactional
    @Logging(module = ModuleName, description = "删除部门", primaryKeyIndex = 1, argNames = "租户ID，部门ID")
    public void delete(long tenantId, long id) {
        Branch branch = branchMapper.selectByPrimaryKey(id);
        if (branch == null || branch.getTenantId() != tenantId) throw new BusinessException("该部门存在子部门，无法删除");
        branch.setDeleted(true);
        branchMapper.updateByPrimaryKey(branch);
        branchCache.clearCache(tenantId);
    }
    @Transactional
    @Logging(module = ModuleName, description = "修改部门信息", primaryKeyIndex = 1, argNames = "租户ID，部门ID，名称，描述，角色ID")
    public void update(long tenantId, long id, String name, String remark, Long parentId, Long[] roleIds) {
        Branch saved = branchMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到对应的部门");
        if (parentId != null) {
            if (parentId != 0) {
                Branch parent = branchMapper.selectByPrimaryKey(parentId);
                if (parent == null) throw new BusinessException("父级部门不存在！");
            }
            saved.setParentId(parentId);
        }
        if (StringUtil.isNotBlank(name)) {
            Branch other = new MapperQuery<>(Branch.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("parentId", saved.getParentId())
                    .andEqualTo("name", name)
                    .andEqualTo("deleted", false)
                    .queryFirst(branchMapper);
            if (other != null && other.getId() != id) throw new BusinessException("已存在同名的部门");
            saved.setName(name);
        }
        if (remark != null) saved.setRemark(remark);
        branchMapper.updateByPrimaryKey(saved);
        if (roleIds != null) privilegeService.setBranchRoles(tenantId, id, roleIds);
        branchCache.clearCache(tenantId);
    }
    public List<Branch> list(long tenantId) {
        return branchCache.list(tenantId);
    }
    public PageInfo<Branch> list(long tenantId, String name, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Branch> branches = new MapperQuery<>(Branch.class)
                .andEqualTo("tenantId", tenantId)
                .andLike("name", "%" + name + "%", StringUtil.isNotBlank(name))
                .andEqualTo("deleted", false)
                .query(branchMapper);
        return new PageInfo<>(branches);
    }
    public List<Branch> tree(long tenantId) {
        return composeBranch(branchMapper.list(tenantId), true);
    }
    public Branch get(long tenantId, long id) {
        return get(tenantId, id, false);
    }
    public Branch get(long tenantId, long id, boolean allowNull) {
        Branch branch = branchMapper.selectByPrimaryKey(id);
        if (!allowNull && (branch == null || branch.getTenantId() != tenantId)) throw new BusinessException("未找到对应的部门");
        return branch;
    }
    private List<Branch> composeBranch(List<Branch> columns, boolean sort) {
        if (columns == null) return null;
        if (sort) columns.sort(Comparator.comparing(Branch::getName));
        Map<Long, Branch> mapColumns = columns.stream().collect(Collectors.toMap(Branch::getId, b-> b));
        Iterator<Map.Entry<Long, Branch>> it = mapColumns.entrySet().iterator();
        List<Branch> result = new ArrayList<>();
        while (it.hasNext()) {
            Branch self = it.next().getValue();
            Branch parent = mapColumns.get(self.getParentId());
            if (parent != null) parent.addChild(self);
            else result.add(self);
        }
        return result;
    }

    public List<Branch> childrenOf(long tenantId, long id, boolean containSelf) {
        return branchCache.childrenOf(tenantId, id, containSelf);
    }
    public List<Long> childrenIdOf(long tenantId, long id, boolean containSelf) {
        List<Branch> branches = childrenOf(tenantId, id, containSelf);
        List<Long> ids = branches == null ? new ArrayList<>() : branches.stream().map(Branch::getId).collect(Collectors.toList());
        return ids;
    }
    public List<Branch> parentOf(long tenantId, long id, boolean containSelf) {
        return branchCache.parentOf(tenantId, id, containSelf);
    }
    public List<Long> parentIdOf(long tenantId, long id, boolean containSelf, boolean addZero) {
        List<Branch> branches = parentOf(tenantId, id, containSelf);
        List<Long> ids = branches == null ? new ArrayList<>() : branches.stream().map(Branch::getId).collect(Collectors.toList());
        if (addZero) ids.add(0L);
        return ids;
    }

    public List<Long> listRoles(long tenantId, long id){
        return privilegeService.getBranchRoleIds(tenantId, id);
    }

    public Long getLogonBranch(long tenantId) {
        Number number = settingService.get(tenantId, ModuleName, Key_LogonBranch, Long.class);
        if (number == null) return null;
        return number.longValue();
    }
    public void setLogonBranch(long tenantId, Long deptId, String deptName) {
        settingService.save(tenantId, ModuleName, Key_LogonBranch, String.valueOf(deptId), deptName);
    }
    public int getMaxLevel(Long tenatnId){
        Number number = settingService.get(tenatnId, ModuleName, Key_MaxLevel, Integer.class);
        if (number == null) return 0;
        else return number.intValue();
    }
}
