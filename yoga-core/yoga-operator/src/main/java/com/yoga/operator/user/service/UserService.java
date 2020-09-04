package com.yoga.operator.user.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.logging.service.LoggingService;
import com.yoga.operator.branch.mapper.BranchMapper;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.duty.mapper.DutyMapper;
import com.yoga.operator.role.service.PrivilegeService;
import com.yoga.operator.user.enums.GenderType;
import com.yoga.operator.user.mapper.UserDataMapper;
import com.yoga.operator.user.mapper.UserMapper;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.model.UserData;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

@Service
@LoggingPrimary(module = UserService.ModuleName, name = "用户管理")
public class UserService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DutyMapper dutyMapper;
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UserDataMapper userDataMapper;

    public final static String Userdata_Signature = "user.signature.fileid";
    public final static String ModuleName = "admin_user";
    public final static String Key_LogonUseCaptcha = "logon.use.captcha";
    public static String getLockForUser(long userId) {
        return UserService.class.getName() + "." + userId;
    }

    @Override
    public String getPrimaryInfo(Object primaryId) {
        User user = userMapper.selectByPrimaryKey(primaryId);
        if (user == null) return null;
        return user.getNickname();
    }

    public List<Long> getRoles(long tenantId, long id){
        return privilegeService.getUserRoleIds(tenantId, id);
    }
    public Set<String> getPrivileges(long tenantId, long id) {
        return privilegeService.getPrivileges(tenantId, id);
    }

    @Transactional
    @Logging(module = ModuleName, description = "添加用户", primaryKeyIndex = -1, excludeArgs = 2, argNames = "租户ID，用户名，，部门ID，职级ID，别名，性别")
    public long add(long tenantId, String username, String password, Long branchId, Long dutyId, String nickname,
                    GenderType gender, String title, String avatar, String mobile, String email, String address,
                    String postcode, String company, Date birthday, Long[] roleIds) {
        User user = null;
        if (StringUtil.isNotBlank(mobile) && new MapperQuery<>(User.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("mobile", mobile)
                .andEqualTo("deleted", false)
                .count(userMapper) > 0) throw new BusinessException("该手机号已经被注册！");
        username = username.toLowerCase();
        if (StringUtil.isNotBlank(username) && new MapperQuery<>(User.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("username", username)
                .andEqualTo("deleted", false)
                .count(userMapper) > 0) throw new BusinessException("该用户名已经被注册！");
        if (dutyId != null && dutyId != 0 && dutyMapper.selectByPrimaryKey(dutyId) == null) throw new BusinessException("无效的职务级别");
        if (branchId != null && branchId != 0 && branchMapper.selectByPrimaryKey(branchId) == null) throw new BusinessException("无效的部门");
        password = DigestUtils.md5Hex(password);
        user = new User(tenantId, username, password, branchId, dutyId, nickname, gender, title, avatar, mobile, email, address, postcode, company, birthday);
        userMapper.insert(user);
        privilegeService.setUserRoles(tenantId, user.getId(), roleIds);
        return user.getId();
    }
    @Logging(module = ModuleName, description = "删除用户", primaryKeyIndex = 1)
    public void delete(long tenantId, long id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null || user.getTenantId() != tenantId) throw new BusinessException("未找到该用户！");
        user.setDeleted(true);
        userMapper.updateByPrimaryKey(user);
    }
    @Logging(module = ModuleName, description = "禁用用户", primaryKeyIndex = 1, argNames = "租户ID，用户ID，是否禁用")
    public void disable(long tenantId, long id, boolean disable) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null || user.getTenantId() != tenantId) throw new BusinessException("未找到该用户！");
        user.setEnabled(!disable);
        userMapper.updateByPrimaryKey(user);
    }

    public User get(long tenantId, long id) {
        return get(tenantId, id, false);
    }
    public User get(long tenantId, long id, boolean allowNull) {
        User user = userMapper.get(tenantId, id);
        if (!allowNull && user == null) throw new BusinessException("未找到该用户！");
        return user;
    }
    public User get(long tenantId, String username) {
        username = username.toLowerCase();
        return new MapperQuery<>(User.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("username", username)
                .andEqualTo("deleted", false)
                .queryFirst(userMapper);
    }
    @Transactional
    @Logging(module = ModuleName, description = "修改用户", primaryKeyIndex = 1, excludeArgs = 3, argNames = "租户ID，，用户名，，部门ID，职级ID，别名，性别")
    public void update(long tenantId, long id, String username, String password, Long branchId, Long dutyId, String nickname,
                       GenderType gender, String title, String avatar, String mobile, String email, String address,
                       String postcode, String company, Date birthday, Long[] roleIds) {
        User saved = userMapper.selectByPrimaryKey(id);
        if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到该用户！");
        if (StringUtil.isNotBlank(username)) {
            username = username.toLowerCase();
            User user = new MapperQuery<>(User.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("username", username)
                    .andEqualTo("deleted", false)
                    .queryFirst(userMapper);
            if (user != null && !user.getId().equals(saved.getId()))throw new BusinessException("该用户名已经被注册！");
            saved.setUsername(username);
        }
        if (StringUtil.isNotBlank(mobile)) {
            User user = new MapperQuery<>(User.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("mobile", mobile)
                    .andEqualTo("deleted", false)
                    .queryFirst(userMapper);
            if (user != null && !user.getId().equals(saved.getId()))throw new BusinessException("该手机号已经被注册！");
            saved.setMobile(mobile);
        }
        if (StringUtil.isNotBlank(nickname)) saved.setNickname(nickname);
        if (avatar != null) saved.setAvatar(avatar);
        if (email != null) saved.setEmail(email);
        if (title != null) saved.setTitle(title);
        if (address != null) saved.setAddress(address);
        if (postcode != null) saved.setPostcode(postcode);
        if (company != null) saved.setCompany(company);
        if (StringUtil.isNotBlank(password)) saved.setPassword(DigestUtils.md5Hex(password));
        if (dutyId != null) saved.setDutyId(dutyId);
        if (branchId != null) saved.setBranchId(branchId);
        if (gender != null) saved.setGender(gender);
        if(birthday!=null) saved.setBirthday(birthday);
        saved.setUpdateTime(new Date());
        userMapper.updateByPrimaryKey(saved);
        if (roleIds != null) privilegeService.setUserRoles(tenantId, id, roleIds);
    }
    public PageInfo<User> list(long tenantId, String name, Long branchId, Long dutyId, String dutyCode, Integer levelAbove, Long excludeBranchId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        if (name != null) name = name.toLowerCase();
        List<User> users = userMapper.list(tenantId, name, branchId, dutyId, dutyCode, levelAbove, excludeBranchId);
        return new PageInfo<>(users);
    }
    public List<User> list(long tenantId, String name, Long branchId, Long dutyId, String dutyCode, Integer levelAbove, Long excludeBranchId) {
        if (name != null) name = name.toLowerCase();
        return userMapper.list(tenantId, name, branchId, dutyId, dutyCode, levelAbove, excludeBranchId);
    }
    /*  branchId 只查询本级，不递归父级和子级
        childBranchId 查询本级以及所有的父级，自动级联
     */
    public PageInfo<User> listOfPrivilege(long tenantId, String privilege, String name, Long branchId, Long dutyId, String dutyCode, Integer levelAbove, Long childBrandId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex + 1, pageSize);
        List<User> users = userMapper.listUserOfPrivilege(tenantId, name, branchId, dutyId, dutyCode, levelAbove, childBrandId, privilege);
        return new PageInfo<>(users);
    }
    public List<User> listOfPrivilege(long tenantId, String privilege, String name, Long branchId, Long dutyId, String dutyCode, Integer levelAbove, Long childBrandId) {
        return userMapper.listUserOfPrivilege(tenantId, name, branchId, dutyId, dutyCode, levelAbove, childBrandId, privilege);
    }
    public void passwd(long tenantId, long id, String oldPwd, String newPwd) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user == null || user.getTenantId() != tenantId) throw new BusinessException("未找到该用户！");
        String hexOldPwd = DigestUtils.md5Hex(oldPwd);
        if (!user.getPassword().equals(hexOldPwd))throw new BusinessException("旧密码验证失败");
        user.setPassword(DigestUtils.md5Hex(newPwd));
        userMapper.updateByPrimaryKey(user);
    }
    public void passwd(long tenantId, String mobile, String password) {
        User user = new MapperQuery<>(User.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("mobile", mobile)
                .andEqualTo("deleted", false)
                .queryFirst(userMapper);
        if (user == null) throw new BusinessException("用户不存在");
        user.setPassword(DigestUtils.md5Hex(password));
        userMapper.updateByPrimaryKey(user);
    }

    public boolean isLogonUseCaptcha(long tenantId) {
        return settingService.get(tenantId, "gcf_user_logon", Key_LogonUseCaptcha, false);
    }

    public List<User> listUserByChildBranchAndLessDuty(long tenantId, Long childBranchId, long lessDutyId) {
        return userMapper.listUserByChildBranchAndLessDuty(tenantId, childBranchId, lessDutyId);
    }
    public User getLowestDutyLevelUserByChildBranch(long tenantId, long childBranchId, long lessDutyId) {
        return userMapper.getLowestDutyLevelUserByChildBranch(tenantId, childBranchId, lessDutyId);
    }
    public User getHightestDutyLevelUserByChildBranch(long tenantId, long childBranchId) {
        return userMapper.getHighestDutyLevelUserByChildBranch(tenantId, childBranchId);
    }
    public List<User> listUserIn(long tenantId, Long...ids) {
        if (ids.length < 1) return new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        for (Long id : ids) {
            if (id != null) userIds.add(id);
        }
        if (userIds.size() < 1) return new ArrayList<>();
        return userMapper.listUserInIds(tenantId, userIds);
    }
    public List<User> listUserIn(long tenantId, Collection<Long> ids) {
        List<Long> userIds = new ArrayList<>();
        for (Long id : ids) {
            if (id != null) userIds.add(id);
        }
        if (userIds.size() < 1) return new ArrayList<>();
        return userMapper.listUserInIds(tenantId, userIds);
    }

    @Transactional
    public User login(long tenantId, String username, String password) {
        username = StringUtil.toLowerCase(username);
        User user = userMapper.getByUserName(tenantId, username);
        if (user == null) throw new BusinessException("用户不存在或者密码错误！");
        String pwd = DigestUtils.md5Hex(password);
        String savedPwd = user.getPassword();
        if (null == savedPwd || !savedPwd.equals(pwd)) throw new BusinessException("用户不存在或者密码错误！");
        user.setLastLogin(new Date());
        userMapper.updateByPrimaryKey(user);
        return user;
    }

    public void setSignatureFileId(long tenantId, long userId, long fileId) {
        setUserData(tenantId, userId, Userdata_Signature, fileId);
    }
    public long getSignatureFileId(long tenantId, long userid) {
        return getUserData(tenantId, userid, Userdata_Signature, 0L);
    }

    public <T> void setUserData(long tenantId, long userId, String key, T value) {
        runInLock(getLockForUser(userId), ()-> {
            UserData userData = new MapperQuery<>(UserData.class)
                    .andEqualTo("userId", userId)
                    .andEqualTo("name", key)
                    .queryFirst(userDataMapper);
            String sValue = null;
            if (value != null) {
                Class<?> clazz = value.getClass();
                if (clazz.isPrimitive()) {
                    sValue = value.toString();
                } else if (clazz == String.class) {
                    sValue = value.toString();
                } else {
                    sValue = JSONObject.toJSONString(value);
                }
            }
            if (userData == null) {
                userData = new UserData(tenantId, userId, key, sValue);
                userDataMapper.insert(userData);
            } else {
                userData.setValue(sValue);
                userDataMapper.updateByPrimaryKey(userData);
            }
        });
    }
    public <T> T getUserData(long tenantId, long userId, String key, Class<?> clazz) {
        UserData userData = new MapperQuery<>(UserData.class)
                .andEqualTo("userId", userId)
                .andEqualTo("name", key)
                .queryFirst(userDataMapper);
        if (userData == null || userData.getTenantId() != tenantId) return null;
        String value = userData.getValue();
        if (value == null) return null;
        if (clazz.isPrimitive()) {
            PropertyEditor editor = PropertyEditorManager.findEditor(clazz);
            editor.setAsText(value);
            return (T) editor.getValue();
        }
        if (clazz == String.class) return (T)value;
        try {
            Method valueOf = clazz.getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) JSONObject.parseObject(value, clazz);
        } catch (Exception ex) {
            return null;
        }
    }
    public <T> T getUserData(long tenantId, long userId, String key, T defaultValue) {
        UserData userData = new MapperQuery<>(UserData.class)
                .andEqualTo("userId", userId)
                .andEqualTo("name", key)
                .queryFirst(userDataMapper);
        if (userData == null || userData.getTenantId() != tenantId) return defaultValue;
        String value = userData.getValue();
        if (value == null) return defaultValue;
        try {
            if (defaultValue.getClass().isPrimitive()) {
                PropertyEditor editor = PropertyEditorManager.findEditor(defaultValue.getClass());
                editor.setAsText(value);
                return (T) editor.getValue();
            }
            if (defaultValue.getClass() == String.class) {
                return (T) value;
            }
            Method valueOf = defaultValue.getClass().getMethod("valueOf", new Class[] { String.class });
            if (valueOf != null) {
                return (T)valueOf.invoke(null, new Object[]{value});
            }
        } catch (Exception ex) {
        }
        try {
            return (T) JSONObject.parseObject(value, defaultValue.getClass());
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
