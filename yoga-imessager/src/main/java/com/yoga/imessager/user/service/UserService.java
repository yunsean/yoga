package com.yoga.imessager.user.service;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.group.model.Group;
import com.yoga.imessager.group.repo.GroupRepository;
import com.yoga.imessager.rongcloud.models.TokenResult;
import com.yoga.imessager.user.model.User;
import com.yoga.imessager.user.repo.UserRepository;
import com.yoga.imessager.rongcloud.service.RongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(value = "imUserService")
public class UserService extends BaseService {

    @Autowired
    private RongService rongService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Group> groups(long tenantId, long userId) {
        List<Group> groups = groupRepository.findByTenantIdAndUserId(tenantId, userId);
        return groups;
    }

    public User get(long tenantId, long userId) {
        User user = userRepository.findFirstByTenantIdAndId(tenantId, userId);
        if (user == null) throw new BusinessException("用户不存在");
        return user;
    }

    public Page<User> find(long tenantId, String nickname, int pageIndex, int pageSize) {
        PageRequest request = new PageRequest(pageIndex, pageSize);
        return userRepository.findByTenantIdAndNickname(tenantId, nickname, request);
    }

    @Transactional
    public void update(long tenantId, long userId, String nickname, String avatar) throws Exception {
        User user = userRepository.findFirstByTenantIdAndId(tenantId, userId);
        if (user == null) throw new BusinessException("用户不存在");
        if (StrUtil.isNotBlank(nickname)) user.setNickname(nickname);
        if (StrUtil.isNotBlank(avatar)) user.setAvatar(avatar);
        userRepository.save(user);
        rongService
                .rongCloud(tenantId)
                .user
                .refresh(String.valueOf(userId), user.getNickname(), user.getAvatar());
    }
}
