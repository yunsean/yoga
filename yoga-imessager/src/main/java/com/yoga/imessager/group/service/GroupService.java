package com.yoga.imessager.group.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.group.enums.UserType;
import com.yoga.imessager.group.model.Group;
import com.yoga.imessager.group.model.GroupUser;
import com.yoga.imessager.group.model.Member;
import com.yoga.imessager.group.repo.GroupRepository;
import com.yoga.imessager.group.repo.GroupUserRepository;
import com.yoga.imessager.rongcloud.messages.ContactNtfMessage;
import com.yoga.imessager.rongcloud.models.CodeSuccessResult;
import com.yoga.imessager.rongcloud.service.RongService;
import com.yoga.imessager.user.model.User;
import com.yoga.imessager.user.model.UserAndType;
import com.yoga.imessager.user.repo.UserRepository;
import com.yoga.user.model.LoginUser;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.user.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService extends BaseService {

    @Autowired
    private RongService rongService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupUserRepository groupUserRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UserService systemUserService;

    public List<Group> find(long tenantId, String name) {
        if (StrUtil.isBlank(name)) return groupRepository.findByTenantId(tenantId);
        else return groupRepository.findByTenantIdAndNameLike(tenantId, "%" + name + "%");
    }

    public Group get(long tenantId, long groupId) {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("群组不存在");
        return group;
    }

    public Group get(long tenantId, String name) {
        Group group = groupRepository.findFirstByTenantIdAndName(tenantId, name);
        if (group == null) throw new BusinessException("群组不存在");
        return group;
    }

    @Transactional
    public void create(long tenantId, String name, String avatar, long creatorId, String creator, String creatorAvatar, List<Member> members) throws Exception {
        Group group = groupRepository.findFirstByTenantIdAndName(tenantId, name);
        if (group != null) throw new BusinessException("已经存在同名组");
        group = new Group(tenantId, name, avatar, creatorId, creator);
        group.setId(sequenceService.getNextValue(com.yoga.imessager.sequence.SequenceNameEnum.SEQ_IM_GROUP_ID));
        groupRepository.save(group);
        GroupUser groupUser = new GroupUser(group.getId(), creatorId, UserType.ADMIN);
        User user = userRepository.findFirstByTenantIdAndId(tenantId, creatorId);
        if (user == null) {
            user = new User(creatorId, tenantId, creatorAvatar, creatorAvatar);
            userRepository.save(user);
        }
        groupUserRepository.save(groupUser);
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .create("" + creatorId, "" + group.getId(), name);
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
        if (members.size() > 0) {
            join(tenantId, group.getId(), UserType.NORMAL, members);
        }
    }

    @Transactional
    public void dismiss(long tenantId, long groupId, long loginId) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("群组不存在");
        groupRepository.delete(groupId);
        groupUserRepository.deleteByGroupId(groupId);
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .dismiss("" + loginId, "" + groupId);
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    @Transactional
    public void rename(long tenantId, long groupId, String name, String avatar) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        if (StrUtil.isNotBlank(name)) group.setName(name);
        if (StrUtil.isNotBlank(avatar)) group.setAvatar(avatar);
        groupRepository.save(group);
        if (StrUtil.isNotBlank(name)) {
            CodeSuccessResult code = rongService
                    .rongCloud(tenantId)
                    .group
                    .refresh("" + groupId, name);
            if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
        }
    }

    @Autowired
    EntityManagerFactory entityManagerFactory;

    public List<UserAndType> users(long tenantId, long groupId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Query query = em.createNamedQuery("findByTenantIdAndGroupId")
                    .setParameter(1, tenantId)
                    .setParameter(2, groupId);
            List<UserAndType> userAndTypes = query.getResultList();
            return userAndTypes;
        } finally {
            em.close();
        }
    }

    @Transactional
    public void apply(long tenantId, long groupId, long userId, String name, String avatar, String message) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        GroupUser groupUser = new GroupUser(groupId, userId, UserType.NORMAL);
        groupUser.setApplying(true);
        groupUserRepository.save(groupUser);
        User user = userRepository.findFirstByTenantIdAndId(tenantId, userId);
        if (user == null) {
            user = new User(userId, tenantId, name, avatar);
            userRepository.save(user);
        }
        List<GroupUser> admins = groupUserRepository.findByGroupIdAndUserTypeAndApplying(groupId, UserType.ADMIN, false);
        if (admins.size() < 1) throw new BusinessException("该群当前无管理员");
        ContactNtfMessage contactNtfMessage = new ContactNtfMessage("Request", String.valueOf(groupId), String.valueOf(userId), String.valueOf(admins.get(0)), message);
        String[] ids = new String[admins.size()];
        for (int i = 0; i < admins.size(); i++) {
            ids[i] = String.valueOf(admins.get(i).getUserId());
        }
        rongService
                .rongCloud(tenantId)
                .message
                .publishPrivate(String.valueOf(userId), ids, contactNtfMessage, "入群请求", "{\"pushData\":\"hello\"}","4", 0, 0, 0, 0);
    }

    @Transactional
    public void accept(long tenantId, long groupId, long loginId, long userId) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        GroupUser login = groupUserRepository.findOneByGroupIdAndUserId(groupId, loginId);
        if (login == null || login.getUserType() != UserType.ADMIN) throw new BusinessException("你不是群管理员");
        GroupUser groupUser = groupUserRepository.findOneByGroupIdAndUserId(groupId, userId);
        if (groupUser == null || !groupUser.isApplying()) throw new BusinessException("该用户当前未申请加入群组");
        groupUser.setApplying(false);
        groupUserRepository.save(groupUser);
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .join(new String[]{"" + userId}, "" + groupId, group.getName());
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    @Transactional
    public void reject(long tenantId, long groupId, long loginId, long userId) {
        GroupUser login = groupUserRepository.findOneByGroupIdAndUserId(groupId, loginId);
        if (login == null || login.getUserType() != UserType.ADMIN) throw new BusinessException("你不是群管理员");
        GroupUser groupUser = groupUserRepository.findOneByGroupIdAndUserId(groupId, userId);
        if (groupUser == null || !groupUser.isApplying()) throw new BusinessException("该用户当前未申请加入群组");
        groupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
    }

    @Transactional
    public void join(long tenantId, long groupId, UserType type, long userId, String name, String avatar) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        GroupUser groupUser = groupUserRepository.findOneByGroupIdAndUserId(groupId, userId);
        if (groupUser != null && !groupUser.isApplying()) {
            throw new BusinessException("该用户已经在群里面");
        } else if (groupUser != null) {
            groupUser.setApplying(false);
        } else {
            groupUser = new GroupUser(groupId, userId, type);
        }
        groupUserRepository.save(groupUser);
        User user = userRepository.findFirstByTenantIdAndId(tenantId, userId);
        if (user == null) {
            user = new User(userId, tenantId, name, avatar);
            userRepository.save(user);
        }
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .join(new String[]{"" + userId}, "" + groupId, group.getName());
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    @Transactional
    public void join(long tenantId, long groupId, UserType type, List<Member> members) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        List<GroupUser> groupUsers = new ArrayList<>();
        String[] ids = new String[members.size()];
        List<User> users = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            groupUsers.add(new GroupUser(groupId, member.getId(), type));
            ids[i] = String.valueOf(member.getId());
            User user = userRepository.findFirstByTenantIdAndId(tenantId, member.getId());
            if (user == null) users.add(new User(member.getId(), tenantId, member.getNickname(), member.getAvatar()));
        }
        if (users.size() > 0) userRepository.save(users);
        groupUserRepository.save(groupUsers);
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .join(ids, "" + groupId, group.getName());
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    @Transactional
    public void quit(long tenantId, long groupId, long userId) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        groupUserRepository.deleteByGroupIdAndUserId(groupId, userId);
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .quit(new String[]{String.valueOf(userId)}, String.valueOf(groupId));
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    @Transactional
    public void quit(long tenantId, long groupId, long[] userIds) throws Exception {
        Group group = groupRepository.findOneByTenantIdAndId(tenantId, groupId);
        if (group == null) throw new BusinessException("组不存在");
        groupUserRepository.deleteByGroupIdAndUserIdIn(groupId, userIds);
        String[] ids = new String[userIds.length];
        for (int i = 0; i < userIds.length; i++) {
            ids[i] = String.valueOf(userIds[i]);
        }
        CodeSuccessResult code = rongService
                .rongCloud(tenantId)
                .group
                .quit(ids, "" + group);
        if (200 != code.getCode()) throw new BusinessException(code.getErrorMessage());
    }

    public final static String Setting_Module = "im_group";
    public final static String Setting_EveryOneGroup = "im.every.one.group";
    public final static long EveryOneGroup_CreatorId = 0;
    public boolean everyOneGroup(long tenantId) {
        return settingService.get(tenantId, Setting_Module, Setting_EveryOneGroup, false);
    }

    public Group getEveryOneGroup(long tenantId) throws Exception {
        Group group = groupRepository.findFirstByTenantIdAndCreatorId(tenantId, EveryOneGroup_CreatorId);
        if (group == null) {
            List<Member> members = new ArrayList<>();
            PageList<com.yoga.user.user.model.User> users = systemUserService.findUsers(tenantId, null, null, null, 0, 1000);
            for (com.yoga.user.user.model.User user : users) {
                members.add(new Member(user.getId(), user.getFullname(), user.getAvatar()));
            }
            create(tenantId, "全员群组", null, EveryOneGroup_CreatorId, "系统管理员", null, members);
            group = groupRepository.findFirstByTenantIdAndCreatorId(tenantId, EveryOneGroup_CreatorId);
        }
        return group;
    }
    public Group ensureUserInEveryOneGroup(long tenantId, LoginUser loginUser) throws Exception {
        Group group = getEveryOneGroup(tenantId);
        GroupUser groupUser = groupUserRepository.findOneByGroupIdAndUserId(group.getId(), loginUser.getId());
        if (groupUser == null) {
            join(tenantId, group.getId(), UserType.NORMAL, loginUser.getId(), loginUser.getNickname(), loginUser.getAvatar());
        }
        return group;
    }
}
