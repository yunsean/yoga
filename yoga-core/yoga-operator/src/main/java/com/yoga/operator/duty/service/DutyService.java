package com.yoga.operator.duty.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.cache.DutyCache;
import com.yoga.operator.duty.mapper.DutyMapper;
import com.yoga.operator.duty.model.Duty;
import com.yoga.operator.role.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@LoggingPrimary(module = DutyService.ModuleName, name = "职级管理")
public class DutyService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private DutyMapper dutyMapper;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private DutyCache dutyCache;

    public final static String ModuleName = "admin_duty";

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Duty duty = dutyMapper.selectByPrimaryKey(primaryId);
        if (duty == null) return null;
        return duty.getName();
    }

    @Transactional
    @Logging(module = ModuleName, description = "添加职务", primaryKeyIndex = -1, argNames = "租户ID，插入点ID，名称，备注，角色ID")
    public long add(long tenantId, long belowId, String name, String code, String remark, Long[] roleIds) {
        if (new MapperQuery<>(Duty.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("name", name)
                .andEqualTo("deleted", false)
                .count(dutyMapper) > 0) throw new BusinessException("已存在同名的职称");
        int level = 0;
        if (belowId != 0) {
            Duty duty = dutyMapper.selectByPrimaryKey(belowId);
            if (duty == null) throw new BusinessException("未找到对应的职称");
            level = duty.getLevel();
        }
        level++;
        Duty duty = new MapperQuery<>(Duty.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("level", level)
                .queryFirst(dutyMapper);
        if (duty != null) dutyMapper.increceLevel(tenantId, level);
        if (StringUtil.isNotBlank(code)) {
            duty = new MapperQuery<>(Duty.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("code", code)
                    .queryFirst(dutyMapper);
            if (duty != null) throw new BusinessException("已存在相同Code的职称");
        }
        duty = new Duty(tenantId, name,level, code, remark);
        dutyMapper.insert(duty);
        privilegeService.setDutyRoles(tenantId, duty.getId(), roleIds);
        dutyCache.clearCache(tenantId);
        return duty.getId();
    }
    @Transactional
    @Logging(module = ModuleName, description = "删除职称", primaryKeyIndex = 1, argNames = "租户ID，职称ID")
    public void delete(long tenantId, long id) {
        Duty duty = dutyMapper.selectByPrimaryKey(id);
        if (duty == null || duty.getTenantId() != tenantId) throw new BusinessException("未找到对应的职称");
        duty.setDeleted(true);
        dutyMapper.updateByPrimaryKey(duty);
        dutyCache.clearCache(tenantId);
    }
    @Transactional
    @Logging(module = ModuleName, description = "修改职称信息", primaryKeyIndex = 1, argNames = "租户ID，职称ID，名称，描述，角色ID")
    public void update(long tenantId, long id, String name, String code, String remark, Long[] roleIds) {
        Duty saved = dutyMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到对应的职称");
        if (StringUtil.isNotBlank(name)) {
            Duty other = new MapperQuery<>(Duty.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("name", name)
                    .andEqualTo("deleted", false)
                    .queryFirst(dutyMapper);
            if (other != null && other.getId() != id) throw new BusinessException("已存在同名的职称");
            saved.setName(name);
        }
        if (StringUtil.isNotBlank(code)) {
            Duty other = new MapperQuery<>(Duty.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("code", code)
                    .queryFirst(dutyMapper);
            if (other != null && other.getId() != id) throw new BusinessException("已存在相同Code的职称");
            saved.setCode(code);
        } else if (code != null) {
            saved.setCode(code);
        }
        if (remark != null) saved.setRemark(remark);
        dutyMapper.updateByPrimaryKey(saved);
        if (roleIds != null) privilegeService.setDutyRoles(tenantId, id, roleIds);
        dutyCache.clearCache(tenantId);
    }
    public PageInfo<Duty> list(long tenantId, String name, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<Duty> duties = dutyMapper.list(tenantId, name);
        return new PageInfo<>(duties);
    }
    public Duty get(long tenantId, long id) {
        Duty duty = dutyMapper.selectByPrimaryKey(id);
        if (duty == null || duty.getTenantId() != tenantId || duty.getDeleted()) throw new BusinessException("未找到对应的职称");
        return duty;
    }
    public Duty getByLevel(long tenantId, int level) {
        Duty duty = new MapperQuery<>(Duty.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("level", level)
                .queryFirst(dutyMapper);
        if (duty == null) throw new BusinessException("未找到对应的职称");
        return duty;
    }
    public Map<String, Duty> map(long tenantId) {
        return dutyCache.map(tenantId);
    }
    public List<Duty> list(long tenantId) {
        return dutyCache.list(tenantId);
    }

    public List<Long> listRoles(long tenantId, long id){
        Duty duty = dutyMapper.selectByPrimaryKey(id);
        if (duty == null || duty.getTenantId() != tenantId) throw new BusinessException("未找到对应的职称");
        return privilegeService.getDutyRoleIds(tenantId, id);
    }
}
