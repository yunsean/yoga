package com.yoga.jiguang.message.service;

import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.RegisterInfo;
import cn.jmessage.api.group.GroupInfoResult;
import cn.jmessage.api.group.GroupListResult;
import cn.jmessage.api.group.MemberListResult;
import cn.jmessage.api.group.MemberResult;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.jiguang.push.JiGuangService;
import com.yoga.jiguang.push.JiGuangSetting;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JMessageGroupService extends BaseService {

    @Autowired
    private JiGuangService jiGuangService;

    public void registerUser(long tenantId, long userId, String nickname, String avatar) {
        try {
            JMessageClient client = getClient(tenantId);
            List<RegisterInfo> users = new ArrayList<>();
            RegisterInfo user = RegisterInfo.newBuilder()
                    .setUsername("uid_" + userId)
                    .setPassword("pwd_" + userId)
                    .setNickname(nickname)
                    .setAvatar(avatar)
                    .build();
            users.add(user);
            RegisterInfo[] regUsers = new RegisterInfo[users.size()];
            client.registerUsers(users.toArray(regUsers));
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    public PageInfo<GroupInfoResult> listGroups(long tenantId, int pageIndex, int pageSize) {
        try {
            JMessageClient client = getClient(tenantId);
            GroupListResult listResult = client.getGroupListByAppkey(pageIndex * pageSize, pageSize);
            PageInfo<GroupInfoResult> result = new PageInfo<>(listResult.getGroups());
            result.setPageNum(pageIndex);
            result.setPageSize(pageSize);
            result.setTotal(listResult.getTotal());
            result.setSize(listResult.getCount());
            return result;
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    public List<MemberResult> listUsers(long tenantId, long groupId) {
        try {
            JMessageClient client = getClient(tenantId);
            MemberListResult infoResult = client.getGroupMembers(groupId);
            if (infoResult.getMembers() == null) return null;
            return Arrays.stream(infoResult.getMembers()).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new BusinessException(ex);
        }
    }

    private JMessageClient getClient(long tenantId) {
        JiGuangSetting setting = jiGuangService.getSetting(tenantId);
        if (setting == null) throw new BusinessException("尚未配置消息服务器！");
        return new JMessageClient(setting.getAppKey(), setting.getMasterSecret());
    }
}
