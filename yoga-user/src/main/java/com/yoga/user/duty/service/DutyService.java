package com.yoga.user.duty.service;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.duty.cache.DutyCache;
import com.yoga.user.duty.dao.DutyDAO;
import com.yoga.user.duty.dto.AddDutyDto;
import com.yoga.user.duty.dto.UpdateDutyDto;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.model.NameAndCount;
import com.yoga.user.duty.repository.DutyRepository;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class DutyService extends BaseService {

    @Autowired
    private DutyRepository dutyRepo;
    @Autowired
    private DutyDAO dutyDAO;
    @Autowired
    private DutyCache dutyCache;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PermissionDAO permissionDAO;

    @Transactional
    public void addDuty(AddDutyDto dto) {
        Duty duty = dutyRepo.findOneByNameAndTenantId(dto.getName(), dto.getTid());
        if (duty != null) throw new BusinessException("已存在同名的职称");

        int level = 0;
        if (dto.getBelowId() != 0) {    //belowId为插入点的职务的ID
            duty = dutyRepo.findOne(dto.getBelowId());
            if (duty == null) throw new BusinessException("未找到对应的职称");
            level = duty.getLevel();
        }
        level++;
        duty = dutyRepo.findFirstByLevelAndTenantId(level, dto.getTid());
        if (duty != null) {
            dutyDAO.advanceDutyLevelAfter(level, dto.getTid());
        }
        duty = new Duty();
        duty.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_U_DUTY_ID));
        duty.setTenantId(dto.getTid());
        duty.setLevel(level);
        duty.setName(dto.getName());
        duty.setRemark(dto.getRemark());
        permissionDAO.setDutyRoles(duty.getTenantId(), duty.getId(), dto.getRoleIds());
        dutyRepo.save(duty);
        dutyCache.clearCache(dto.getTid());
    }

    public List getDutyRoles(long tenantId, long dutyId){
        return dutyRepo.getDutyRoles(tenantId, dutyId);
    }

    @Transactional
    public void delDuty(long tid, long id) {
        Duty duty = dutyRepo.findOne(id);
        if (duty == null) throw new BusinessException("未找到对应的职称");
        permissionDAO.delDutyRoles(duty.getTenantId(), duty.getId());
        dutyDAO.deleteDutyByLevel(duty.getLevel(), duty.getTenantId());
        dutyCache.clearCache(tid);
    }

    @Transactional
    public void updateDuty(UpdateDutyDto updateDutyDto) {
        Duty duty = dutyRepo.findOne(updateDutyDto.getId());
        if (duty == null) throw new BusinessException("未找到对应的职称");
        Duty other = dutyRepo.findOneByNameAndTenantId(updateDutyDto.getName(), updateDutyDto.getTid());
        if (other != null && other.getId() != duty.getId()) throw new BusinessException("已存在同名的职称");
        if (updateDutyDto.getRoleIds() != null && updateDutyDto.getRoleIds().length > 0) {
            permissionDAO.setDutyRoles(duty.getTenantId(), duty.getId(), updateDutyDto.getRoleIds());
        }
        if (StrUtil.isNotBlank(updateDutyDto.getName())) duty.setName(updateDutyDto.getName());
        if (updateDutyDto.getRemark() != null) duty.setRemark(updateDutyDto.getRemark());
        dutyRepo.save(duty);
        dutyCache.clearCache(updateDutyDto.getTid());
    }

    public List<Duty> allDuties(long tenantId) {
        List<Duty> duties = dutyRepo.findAllByTenantIdOrderByLevelDesc(tenantId);
        return duties;
    }

    public List<Duty> getDuties(long tenantId) {
        return dutyCache.getDuties(tenantId);
    }
    public Map<String, Duty> getDutyMap(long tenantId) {
        return dutyCache.dutyMap(tenantId);
    }

    public List<NameAndCount> countRank(long tenantId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String sql = "select d.id id, d.name name, count(d.id) count from s_user u inner join s_duty d on u.duty_id = d.id"
                + " where u.tenant_id = ? "
                + "group by d.id order by count desc";
        try {
            int index = 0;
            Query query = em.createNativeQuery(sql, "ReturnDutyNameAndCount")
                    .setParameter(++index, tenantId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Duty get(long tenantId, Long dutyId) {
        if (dutyId == null) return null;
        Map<String, Duty> map = dutyCache.dutyMap(tenantId);
        return map.get(String.valueOf(dutyId));
    }

    public String getName(long tenantId, Long dutyId) {
        Duty duty = get(tenantId, dutyId);
        if (duty == null) return null;
        return duty.getName();
    }
}
