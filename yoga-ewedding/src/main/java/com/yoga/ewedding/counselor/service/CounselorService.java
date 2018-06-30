package com.yoga.ewedding.counselor.service;


import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.DateUtil;
import com.yoga.core.utils.StrUtil;
import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.ewedding.counselor.mapper.CounselorMapper;
import com.yoga.ewedding.counselor.model.Counselor;
import com.yoga.ewedding.counselor.model.CounselorUser;
import com.yoga.ewedding.counselor.repo.CounselorRepository;
import com.yoga.ewedding.recharge.enums.RechargeType;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.user.user.enums.GenderType;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import com.yoga.utility.aliyun.OCRIdCardRecognizer;
import com.yoga.utility.aliyun.OCRIdCardResult;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("eweddingCounselorService")
public class CounselorService extends BaseService {

    @Autowired
    private CounselorRepository counselorRepository;
    @Autowired
    private CounselorMapper counselorMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private OCRIdCardRecognizer idCardRecognizer;

    @Transactional
    public Counselor add(long tenantId, long deptId, String mobile, String password, String lastname, String firstname, String avatar, String email, String qq, String title, String company, String wechat, String intro, GenderType gender) {
        long id = userService.addUser(tenantId, mobile, password, lastname, firstname, deptId, 0, null, avatar, mobile, email, qq, null, null, null, company, wechat, null, null, intro, null, null, gender, true, null);
        Counselor counselor = new Counselor(id, tenantId);
        counselor.setProveToken(UUID.randomUUID().toString());
        counselorRepository.save(counselor);
        return counselor;
    }

    public CounselorStatus prove(long tenantId, String token, String pid, String pidFront, String pidBack, String[] images) throws AuthenticationException {
        Counselor counselor = counselorRepository.findOneByTenantIdAndProveToken(tenantId, token);
        if (counselor == null) throw new BusinessException("未找到顾问信息！");
        if (!StrUtil.isEqual(token, counselor.getProveToken())) throw new AuthenticationException("请登录！");
        if (counselor.getStatus() == CounselorStatus.accepted) throw new BusinessException("顾问已经通过审核！");
        if (StrUtil.isNotBlank(pid)) counselor.setPid(pid);
        if (StrUtil.isNotBlank(pidFront)) {
            if (forceRecognizeIdCard(tenantId) && !pidFront.equalsIgnoreCase(counselor.getPidFront())) {
                try {
                    OCRIdCardResult result = idCardRecognizer.recognize(tenantId, pidFront);
                    if (!StrUtil.isEqual(result.getNumber(), pid)) throw new Exception("身份证号码不正确");
                    Date birthday = DateUtil.toDate(result.getBirthday(), "yyyyMMdd");
                    counselor.set(result.getNumber(), result.getName(), result.getNation(), birthday, result.getSex(), result.getAddress(), null);
                } catch (Exception ex) {
                    throw new BusinessException("嗯哼，隔壁老马说他不能识别你的身份证照片，请重新上传吧！");
                }
            }
            counselor.setPidFront(pidFront);
        }
        if (StrUtil.isNotBlank(pidBack)) counselor.setPidBack(pidBack);
        if (images != null) counselor.setImageList(images);
        if (StrUtil.hasNotBlank(counselor.getPid(), counselor.getPidFront(), counselor.getPidBack())) {
            counselor.setStatus(CounselorStatus.checking);
        } else {
            counselor.setStatus(CounselorStatus.filling);
        }
        counselorRepository.save(counselor);
        return counselor.getStatus();
    }

    public void update(long tenantId, String token, Long deptId, String lastname, String firstname, String avatar, String email, String qq, String title, String company, String wechat, String intro, GenderType gender) throws AuthenticationException {
        Counselor counselor = counselorRepository.findOneByTenantIdAndProveToken(tenantId, token);
        if (counselor == null) throw new BusinessException("未找到顾问信息！");
        if (counselor == null || counselor.getTenantId() != tenantId) throw new BusinessException("未找到顾问信息！");
        if (!StrUtil.isEqual(token, counselor.getProveToken())) throw new AuthenticationException("请登录！");
        if (counselor.getStatus() == CounselorStatus.accepted) throw new BusinessException("顾问已经通过审核！");
        userService.updateUser(tenantId, null, counselor.getId(), null, null, firstname, lastname, avatar, email, qq, null, title, null, null, company, wechat, null, null, intro, null, null, deptId, null, gender, null);
    }

    public Counselor get(long tenantId, long userId, boolean refreshProveToken) {
        Counselor counselor = counselorRepository.findOne(userId);
        if (counselor == null || counselor.getTenantId() != tenantId) throw new BusinessException("未找到顾问信息！");
        if (refreshProveToken && counselor.getStatus() != CounselorStatus.accepted) {
            counselor.setProveToken(UUID.randomUUID().toString());
            counselorRepository.save(counselor);
        }
        return counselor;
    }

    public CounselorUser get(long tenantId, long id) {
        CounselorUser user = counselorMapper.findByTenantIdAndId(tenantId, id);
        if (user == null) throw new BusinessException("未找到顾问信息！");
        return user;
    }

    public void verify(long tenantId, long id, boolean rejected, String reason) {
        Counselor counselor = counselorRepository.findOne(id);
        if (counselor == null || counselor.getTenantId() != tenantId) throw new BusinessException("未找到顾问信息！");
        if (counselor.getStatus() == CounselorStatus.accepted) throw new BusinessException("顾问已经通过审核！");
        counselor.setStatus(rejected ? CounselorStatus.rejected : CounselorStatus.accepted);
        counselor.setRejectReason(reason);
        if (!rejected) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, experienceDays(tenantId));
            counselor.setExpire(calendar.getTime());
        }
        counselorRepository.save(counselor);
    }

    public void delete(long tenantId, long id) {
        User user = userService.getUser(tenantId, id);
        Counselor counselor = counselorRepository.findOne(id);
        if (counselor != null) counselorRepository.delete(counselor);
        userService.deleteUser(tenantId, id);
    }

    public PageList<CounselorUser> list(long tenantId, Long typeId, CounselorStatus status, String name, String company, int pageIndex, int pageSize) {
        int startIndex = pageIndex * pageSize;
        int totalCount = counselorMapper.countBy(tenantId, typeId, status, name, company);
        if (startIndex >= totalCount) return new PageList<>(null, pageIndex, pageSize, totalCount);
        List<CounselorUser> counselorUsers = counselorMapper.findBy(tenantId, typeId, status, name, company, startIndex, pageSize);
        return new PageList<>(counselorUsers, pageIndex, pageSize, totalCount);
    }

    public List<CounselorUser> findByAboveId(long tenantId, Long aboveId) {
        List<CounselorUser> counselorUsers = counselorMapper.findAboveId(tenantId, aboveId);
        return counselorUsers;
    }

    public Date[] prepareRecharge(long tenantId, long userId, RechargeType type, Date expireTo) {
        Counselor counselor = counselorRepository.findOne(userId);
        if (counselor == null || counselor.getTenantId() != tenantId) throw new BusinessException("顾问不存在！");
        Calendar begin = Calendar.getInstance();
        if (counselor.getExpire() != null) begin.setTime(counselor.getExpire());
        Calendar end = Calendar.getInstance();
        end.setTime(begin.getTime());
        switch (type) {
            case monthly:
                end.add(Calendar.MONTH, 1);
                break;
            case quarterly:
                end.add(Calendar.MONTH, 3);
                break;
            case halfyear:
                end.add(Calendar.MONTH, 6);
                break;
            case yearly:
                end.add(Calendar.YEAR, 1);
                break;
        }
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 0);
        if (expireTo != null && expireTo.after(end.getTime())) throw new BusinessException("充值时间不正常，请重新进入充值页面！");
        return new Date[]{begin.getTime(), end.getTime()};
    }

    public final static String Module_Name = "ew_counselor";
    public final static String DepartmentId_Key = "counselor.department.id";
    public final static String IDCardRecognize_Key = "counselor.idcard.recognize";
    public final static String ExperienceDays_Key = "counselor.experience.dayes";
    public Long getDepartmentId(long tenantId) {
        Number id = settingService.get(tenantId, Module_Name, DepartmentId_Key, Long.class);
        if (id == null) return null;
        return id.longValue();
    }
    public boolean forceRecognizeIdCard(long tenantId) {
        return settingService.get(tenantId, Module_Name, IDCardRecognize_Key, true);
    }
    public int experienceDays(long tenantId) {
        return settingService.get(tenantId, Module_Name, ExperienceDays_Key, 5);
    }
}
