package com.yoga.moment.group.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.moment.group.ao.UserForGroup;
import com.yoga.moment.group.mapper.MomentGroupMapper;
import com.yoga.moment.group.mapper.MomentGroupUserMapper;
import com.yoga.moment.group.model.MomentGroup;
import com.yoga.moment.group.model.MomentGroupUser;
import com.yoga.moment.message.mapper.MomentFollowMapper;
import com.yoga.moment.message.mapper.MomentMessageMapper;
import com.yoga.moment.message.mapper.MomentUpvoteMapper;
import com.yoga.moment.message.model.MomentFollow;
import com.yoga.moment.message.model.MomentMessage;
import com.yoga.moment.message.model.MomentUpvote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class MomentGroupService extends BaseService {

    @Autowired
    private MomentGroupMapper groupMapper;
    @Autowired
    private MomentGroupUserMapper groupUserMapper;

    @Transient
    public Long add(long tenantId, String name, String remark) {
        long existed = new MapperQuery<>(MomentGroup.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("name", name)
                .count(groupMapper);
        if (existed > 0) throw new BusinessException("已经存在同名分组！");
        MomentGroup group = new MomentGroup(tenantId, name, remark);
        groupMapper.insert(group);
        return group.getId();
    }
    @Transient
    public void delete(long tenantId, long id) {
        MomentGroup group = groupMapper.selectByPrimaryKey(id);
        if (group == null || group.getTenantId() != tenantId) throw new BusinessException("指定分组不存在！");
        group.setDeleted(true);
        groupMapper.updateByPrimaryKey(group);
    }
    @Transient
    public void update(long tenantId, long id, String name, String remark) {
        MomentGroup group = groupMapper.selectByPrimaryKey(id);
        if (group == null || group.getTenantId() != tenantId) throw new BusinessException("指定分组不存在！");
        if (StringUtil.isNotBlank(name)) group.setName(name);
        if (remark != null) group.setRemark(remark);
        groupMapper.updateByPrimaryKey(group);
    }
    public PageInfo<MomentGroup> list(long tenantId, String filter, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<MomentGroup> groups = groupMapper.list(tenantId, filter);
        return new PageInfo<>(groups);
    }
    public List<MomentGroup> listForUser(long tenantId, long userId) {
        return groupMapper.listGroup(userId);
    }
    public MomentGroup get(long tenantId, long id) {
        MomentGroup group = groupMapper.selectByPrimaryKey(id);
        if (group == null || group.getTenantId() != tenantId) throw new BusinessException("指定分组不存在！");
        return group;
    }

    @Transient
    public void addUser(long tenantId, long groupId, long userId) {
        MomentGroupUser user = new MapperQuery<>(MomentGroupUser.class)
                .andEqualTo("groupId", groupId)
                .andEqualTo("userId", userId)
                .queryFirst(groupUserMapper);
        if (user != null) return;
        user = new MomentGroupUser(groupId, userId);
        groupUserMapper.insert(user);
    }
    @Transient
    public void deleteUser(long tenantId, long groupId, long userId) {
        MomentGroupUser user = new MapperQuery<>(MomentGroupUser.class)
                .andEqualTo("groupId", groupId)
                .andEqualTo("userId", userId)
                .queryFirst(groupUserMapper);
        if (user == null) return;
        groupUserMapper.delete(user);
    }
    @Transient
    public void updateUser(long tenantId, long groupId, Long[] addIds, Long[] deleteIds) {
        if (addIds != null) {
            Arrays.stream(addIds).forEach(userId-> {
                MomentGroupUser user = new MapperQuery<>(MomentGroupUser.class)
                        .andEqualTo("groupId", groupId)
                        .andEqualTo("userId", userId)
                        .queryFirst(groupUserMapper);
                if (user != null) return;
                groupUserMapper.insert(new MomentGroupUser(groupId, userId));
            });
        }
        if (deleteIds != null) {
            Arrays.stream(deleteIds).forEach(userId-> {
                MomentGroupUser user = new MapperQuery<>(MomentGroupUser.class)
                        .andEqualTo("groupId", groupId)
                        .andEqualTo("userId", userId)
                        .queryFirst(groupUserMapper);
                if (user == null) return;
                groupUserMapper.delete(user);
            });
        }
    }
    public PageInfo<MomentGroupUser> listUser(long tenantId, long groupId, String filter, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<MomentGroupUser> users = groupUserMapper.listUser(groupId, filter);
        return new PageInfo<>(users);
    }
    public PageInfo<UserForGroup> allUser(long tenantId, long groupId, Long branchId, Long dutyId, String filter, boolean includedOnly, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<UserForGroup> users = groupUserMapper.allUser(tenantId, groupId, branchId, dutyId, filter, includedOnly);
        return new PageInfo<>(users);
    }
}
