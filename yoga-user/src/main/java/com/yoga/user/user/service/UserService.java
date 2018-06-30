package com.yoga.user.user.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.SerializationUtils;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.constants.CachePrefixConstants;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.repo.DepartmentRepository;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.repository.DutyRepository;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.user.shiro.RedisSessionDAO;
import com.yoga.user.user.cache.UserCache;
import com.yoga.user.user.dto.AddDto;
import com.yoga.user.user.enums.GenderType;
import com.yoga.user.user.model.User;
import com.yoga.user.user.repo.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserService extends BaseService {

    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserCache userCache;
    @Autowired
    private DutyRepository dutyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private PermissionDAO permissionDAO;

    @Autowired
    private StringRedisTemplate redis = null;

    public List<Long> getRoles(long tenantId, long userId){
        return userRepo.getUserRoles(tenantId, userId);
    }

    public User getLoginInfo() {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return user;
    }

    public Set<String> getPermissions(User user) {
        return permissionDAO.getUserPermissions(user);
    }

    public void updateUserCache(String deviceId) {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) return;
        String token = (String) session.getId();
        String username = user.getUsername();
        String redisKey = CachePrefixConstants.USER_UID + username + "@" + deviceId;
        String old = redis.opsForValue().get(redisKey);
        if (old != null) {
            try {
                Session sessionOld = sessionDAO.readSession(old);
                if (sessionOld != null) {
                    sessionOld.setTimeout(1);
                    sessionOld.stop();
                }
                String oldKey = SerializationUtils.sessionKey(RedisSessionDAO.getRedisSessionKeyPrefix(), old);
                redis.delete(oldKey);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        redis.opsForValue().set(redisKey, token);
    }

    @Transactional
    public long addUser(long tenantId, String username, String password, String lastname, String firstname, long deptId, long dutyId, long[] roleIds, String avatar, String phone, String email, String qq, String title, String address, String postcode, String company, String wechat, Date birthday, Long extLong, String extText, Integer extInt, Double extDouble, GenderType gender, boolean disabled, Date expire) {
        User user = null;
        if (StrUtil.isNotBlank(phone)) {
            user = userRepo.findFirstByTenantIdAndPhone(tenantId, phone);
            if (user != null) throw new BusinessException("该手机号已经被注册！" + phone);
        }
        if (StrUtil.isNotBlank(username)) {
            user = userRepo.findFirstByTenantIdAndUsername(tenantId, username);
            if (user != null) throw new BusinessException("该用户名已经被注册！" + username);
        }
        if (dutyId != 0) {
            Duty duty = dutyRepository.findFirstByTenantIdAndId(tenantId, dutyId);
            if (duty == null) throw new BusinessException("无效的职务级别");
        }
        if (deptId != 0) {
            Department dept = departmentRepository.findOneByTenantIdAndId(tenantId, deptId);
            if (dept == null) throw new BusinessException("无效的部门");
        }
        String pwd = DigestUtils.md5Hex(password);
        user = new User(tenantId, username, pwd, deptId, dutyId, firstname, lastname, title, avatar, phone, email, qq, address, postcode, company, wechat, disabled, birthday, expire, extLong, extText, extInt, extDouble, gender);
        user.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_U_USER_ID));
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        userRepo.save(user);
        permissionDAO.setUserRoles(user.getTenantId(), user.getId(), roleIds);
        return user.getId();
    }

    @Transactional
    public void addUser(AddDto dto) {
        addUser(dto.getTid(), dto.getUsername(), dto.getPassword(), dto.getLastname(), dto.getFirstname(), dto.getDeptId(), dto.getDutyId(), dto.getRoleIds(), dto.getAvatar(), dto.getPhone(), dto.getEmail(), dto.getQq(), dto.getTitle(), dto.getAddress(), dto.getPostcode(), dto.getCompany(), dto.getWechat(), dto.getBirthday(), dto.getExtLong(), dto.getExtText(), dto.getExtInt(), dto.getExtDouble(), dto.getGender(), false, null);
    }

    public User getUser(long tenantId, long userId) {
        User user = userRepo.findOne(userId);
        if (user == null || user.getTenantId() != tenantId) throw new BusinessException("未找到该用户！");
        if (user.getLastname() != null) user.setFullname(user.getLastname() + user.getFirstname());
        return user;
    }

    @Transactional
    public User updateUser(long tenantId,Date birthday, long userId, String username, String password, String firstName, String lastName, String avatar, String email, String qq, String mobile, String title, String address, String postcode,
                           String company, String wechat, String phone, Long extLong, String extText, Integer extInt, Double extDouble, Long deptId, Long dutyId, GenderType gender, long[] roleIds) {
        User saved = userRepo.findFirstByTenantIdAndId(tenantId, userId);
        if (null == saved) throw new BusinessException("没有查找到指定用户");
        if (!StrUtil.isBlank(username)) {
            User user = userRepo.findFirstByTenantIdAndUsername(tenantId, username);
            if (user != null && user.getId() != saved.getId())throw new BusinessException("该用户名已经被注册！");
            saved.setUsername(username);
        }
        if (!StrUtil.isBlank(phone)) {
            User user = userRepo.findFirstByTenantIdAndPhone(tenantId, phone);
            if (user != null && user.getId() != saved.getId())throw new BusinessException("该手机号已经被注册！");
            saved.setPhone(phone);
        }
        if (!StrUtil.isBlank(firstName)) saved.setFirstname(firstName);
        if (!StrUtil.isBlank(lastName)) saved.setLastname(lastName);
        saved.updateFullname();
        if (!StrUtil.isBlank(avatar)) saved.setAvatar(avatar);
        if (!StrUtil.isBlank(email)) saved.setEmail(email);
        if (!StrUtil.isBlank(mobile)) saved.setPhone(mobile);
        if (!StrUtil.isBlank(title)) saved.setTitle(title);
        if (!StrUtil.isBlank(address)) saved.setAddress(address);
        if (!StrUtil.isBlank(postcode)) saved.setPostcode(postcode);
        if (!StrUtil.isBlank(qq)) saved.setQq(qq);
        if (!StrUtil.isBlank(company)) saved.setCompany(company);
        if (!StrUtil.isBlank(wechat)) saved.setWechat(wechat);
        if (!StrUtil.isBlank(password)) saved.setPassword(DigestUtils.md5Hex(password));
        if (extLong != null) saved.setExtLong(extLong);
        if (extText != null) saved.setExtText(extText);
        if (extInt != null) saved.setExtInt(extInt);
        if (extDouble != null) saved.setExtDouble(extDouble);
        if (deptId != null) saved.setDeptId(deptId);
        if (dutyId != null) saved.setDutyId(dutyId);
        if (gender != null) saved.setGender(gender);
        if(birthday!=null) saved.setBirthday(birthday);
        saved.setUpdateTime(new Date());
        userRepo.save(saved);
        if (roleIds != null) {
            permissionDAO.setUserRoles(saved.getTenantId(), saved.getId(), roleIds);
        }
        return saved;
    }

    public PageList<User> findUsers(long tenantId, String name, Long departmentId, Long dutyId, int pageIndex, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<User> page = userRepo.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> r, CriteriaQuery<?> q, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                List<Expression<Boolean>> expressions = predicate.getExpressions();
                expressions.add(cb.equal(r.get("tenantId"), tenantId));
                expressions.add(cb.equal(r.get("disabled"), false));
                if (departmentId != null && departmentId == 0) {  //不能加0判断，因为用户列表页有"未指定选项"，未指定部门的deptID为0
                    expressions.add(cb.or(
                            cb.equal(r.<Long>get("deptId"), departmentId),
                            cb.isNull(r.<Long>get("deptId"))));
                } else if (departmentId != null) {
                    expressions.add(cb.equal(r.<Long>get("deptId"), departmentId));
                }
                if (dutyId != null && dutyId == 0) {
                    expressions.add(cb.or(
                            cb.equal(r.<Long>get("dutyId"), dutyId),
                            cb.isNull(r.<Long>get("dutyId"))));
                } else if (dutyId != null) {
                    expressions.add(cb.equal(r.<Long>get("dutyId"), dutyId));
                }
                if (StrUtil.isNotBlank(name)){
                    expressions.add(cb.or(cb.like(r.<String>get("username"), "%" + name + "%"),
                            cb.like(r.<String>get("fullname"), "%" + name + "%")));
                }
                expressions.add(cb.equal(r.get("tenantId"), tenantId));
                return predicate;
            }
        }, pageable);
        return new PageList<>(page);
    }

    public void modifyPassword(long userId, String oldPwd, String newPwd) {
        User user = userRepo.findOne(userId);
        if (null == user) throw new BusinessException("用户不存在");
        String hexOldPwd = DigestUtils.md5Hex(oldPwd);
        if (!user.getPassword().equals(hexOldPwd))throw new BusinessException("旧密码验证失败");
        user.setPassword(DigestUtils.md5Hex(newPwd));
        userRepo.save(user);
    }

    public void updatePassword(long tenantId, String mobile, String password) {
        User user = userRepo.findFirstByTenantIdAndPhone(tenantId, mobile);
        if (user == null) user = userRepo.findFirstByTenantIdAndUsername(tenantId, mobile);
        if (user == null) throw new BusinessException("用户不存在");
        user.setPassword(DigestUtils.md5Hex(password));
        userRepo.save(user);
    }

    public boolean disableUser(boolean disabled, long userId) {
        return userRepo.setDisabled(userId, disabled ? 1 : 0) > 0;
    }

    public User getUserInfo(long userId) {
        User user = userRepo.findOne(userId);
        return user;
    }

    public PageList<User> getUserHasPrivilege(long tenantId, String[] privilege, String op, int pageIndex, int pageSize) {
        return userCache.findUserHasPrivilege(privilege, op, tenantId, pageIndex, pageSize);
    }

    public void deleteUser(long tenantId, long id) {
        User user = userRepo.findOne(id);
        if (user != null && user.getTenantId() == tenantId) {
            permissionDAO.delUserRoles(user.getTenantId(), id);
            userRepo.delete(id);
        }
    }
}
