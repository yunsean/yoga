package com.yoga.user.dept.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.sequence.SequenceService;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.cache.DepartmentCache;
import com.yoga.user.dept.dto.AddDeptDto;
import com.yoga.user.dept.dto.DelDeptDto;
import com.yoga.user.dept.dto.UpdateDeptDto;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.model.NameAndCount;
import com.yoga.user.dept.repo.DepartmentRepository;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.*;

@Service
public class DepartmentService extends BaseService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private SequenceService sequence;
    @Autowired
    private DepartmentCache deptCache;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private SettingService settingService;
    @Autowired
    private DepartmentCache departmentCache;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public List getDeptRoles(long tenantId, long deptId){
        return departmentRepository.getDeptRoles(tenantId, deptId);
    }

    @Transactional
    public void add(AddDeptDto dto) {
        if (departmentRepository.countByTenantIdAndName(dto.getTid(), dto.getName()) > 0) throw new BusinessException("已经存在同名部门");
        if (dto.getParentId() != 0) {
            Department parent = departmentRepository.findOneByTenantIdAndId(dto.getTid(), dto.getParentId());
            if (parent == null) throw new BusinessException("父级部门不存在！");
            int maxLevel = getMaxLevel(dto.getTid());
            if (maxLevel > 0 && parent.getCode().length() >= maxLevel * 2) throw new BusinessException("最多只允许创建" + maxLevel + "级部门！");
        }
        String code = nextCode(dto.getTid(), dto.getParentId());
        Department dept = new Department();
        dept.setId(sequence.getNextValue(SequenceNameEnum.SEQ_DC_DEPT_ID));
        dept.setCode(code);
        dept.setTenantId(dto.getTid());
        dept.setParentId(dto.getParentId());
        dept.setName(dto.getName());
        dept.setRemark(dto.getRemark());
        dept.setCreateTime(new Date());
        permissionDAO.setDepartmentRoles(dept.getTenantId(), dept.getId(), dto.getRoleIds());
        departmentRepository.save(dept);
        deptCache.clearDepartmentCache(dto.getTid());
    }

    @Transactional
    public void delete(DelDeptDto delDept) {
        long count = departmentRepository.countByTenantIdAndParentId(delDept.getTid(), delDept.getId());
        if (count > 0) throw new BusinessException("该部门存在子部门，无法删除");
        Department department = departmentRepository.findOneByTenantIdAndId(delDept.getTid(), delDept.getId());
        if (department == null) throw new BusinessException("未找到该部门");
        departmentRepository.deleteByTenantIdAndId(delDept.getTid(), delDept.getId());
        permissionDAO.delDepartmentRole(delDept.getTid(), delDept.getId());
        deptCache.clearDepartmentCache(delDept.getTid());
    }

    @Transactional
    public void update(UpdateDeptDto dto) {
        Department saved = departmentRepository.findOneByTenantIdAndId(dto.getTid(), dto.getId());
        if (null == saved) throw new BusinessException("未找到该部门");
        if (StrUtil.isNotBlank(dto.getName())) {
            Department department = departmentRepository.findByNameAndTenantId(dto.getName(), dto.getTid());
            if (department != null && department.getId() != saved.getId()) throw new BusinessException("已经存在同名部门");
            saved.setName(dto.getName());
        }
        if (dto.getRemark() != null)saved.setRemark(dto.getRemark());
        if (dto.getRoleIds() != null && dto.getRoleIds().length > 0) {
            permissionDAO.setDepartmentRoles(saved.getTenantId(), saved.getId(), dto.getRoleIds());
        }
        departmentRepository.save(saved);
        deptCache.clearDepartmentCache(dto.getTid());
    }

    public List<Department> list(long tenantId){
        return departmentRepository.findByTenantId(tenantId);
    }
    public List<Department> treeOfAll(long tenantId) {
        return composeDepartment(list(tenantId), true);
    }
    public List<Department> treeOfParent(long tenantId, long parentId) {
        Department department = departmentRepository.findOneByTenantIdAndId(tenantId, parentId);
        if (department == null) throw new BusinessException("指定部门不存在！");
        return composeDepartment(queryChildrenById(parentId), true);
    }

    private List<Department> composeDepartment(List<Department> columns, boolean resort) {
        if (resort) {
            Collections.sort(columns, new Comparator<Department>() {
                @Override
                public int compare(Department o1, Department o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        Map<Long, Department> mapColumns = new HashMap<>();
        for (Department cmsColumn : columns) {
            mapColumns.put(cmsColumn.getId(), cmsColumn);
            cmsColumn.initChildren();
        }
        Iterator<Map.Entry<Long, Department>> it = mapColumns.entrySet().iterator();
        List<Department> result = new ArrayList<>();
        while(it.hasNext()) {
            Department self = it.next().getValue();
            Department parent = mapColumns.get(self.getParentId());
            if (parent != null) {
                parent.addChild(self);
            } else {
                result.add(self);
            }
        }
        return result;
    }
    public List<Department> treeOfQuery(String deptNameLike, long tenantId) {
        TenantPage page = new TenantPage(0, 10000);
        return composeDepartment(query(deptNameLike, page, tenantId), true);
    }

    public PageList<Department> query(String deptNameLike, TenantPage commonPage, long tenantId) {
        Page<Department> departmentPage = departmentRepository.findAll(new Specification<Department>() {
            @Override
            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (StrUtil.isNotBlank(deptNameLike)) {
                    predicate.getExpressions().add(cb.like(root.get("name"), "%" + deptNameLike + "%"));
                    predicate.getExpressions().add(cb.equal(root.get("tenantId"), tenantId));
                }
                predicate.getExpressions().add(cb.equal(root.get("tenantId"), tenantId));
                return predicate;
            }
        }, commonPage.asPageRequest());

        return new PageList<Department>(departmentPage);
    }

    public List<Department> queryParentById(long selfId) {
        return departmentRepository.queryParentById(selfId);
    }
    public List<Long> queryParentIdById(long selfId) {
        List<BigInteger> ids = departmentRepository.queryParentIdById(selfId);
        List<Long> result = new ArrayList<>();
        for (BigInteger id : ids) {
            result.add(id.longValue());
        }
        return result;
    }

    public List<Department> queryChildrenById(long parentId) {
        return departmentRepository.queryChildrenById(parentId);
    }
    public List<Long> queryChildrenIdById(long parentId) {
        List<BigInteger> ids = departmentRepository.queryChildrenIdById(parentId);
        List<Long> result = new ArrayList<>();
        for (BigInteger id : ids) {
            result.add(id.longValue());
        }
        return result;
    }

    public Department get(long tenantId, Long id) {
        if (id == null) return null;
        Map<String, Department> map = departmentCache.deptMap(tenantId);
        return map.get(String.valueOf(id));
    }
    public String getName(long tenantId, Long id) {
        Department department = get(tenantId, id);
        if (department == null) return null;
        return department.getName();
    }

    public Iterable<Department> getDeptDictionaryApp(long tendentId) {
        return departmentRepository.findByTenantId(tendentId);
    }

    public List<Map<Long, String>> getDeptDictionaryWeb(long tenantId) {
        return deptCache.getDeptDictionaryWeb(tenantId);
    }
    public Map<String, Department> getDeptMap(long tenantId) {
        return deptCache.deptMap(tenantId);
    }

    public List<NameAndCount> countRank(long tenantId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        String sql = "select d.id id, d.name name, count(d.id) count from s_user u inner join s_department d on u.dept_id = d.id"
                + " where u.tenant_id = ? "
                + "group by d.id order by count desc";
        try {
            int index = 0;
            Query query = em.createNativeQuery(sql, "ReturnDeptNameAndCount")
                    .setParameter(++index, tenantId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public final static String Setting_Module_UserLogon = "gcf_user_logon";
    public final static String Setting_Module = "pri_dept";
    public final static String DefaultDept_Key = "defaultRegDept";
    public final static String LogonRequireMobile_Key = "logon.require.mobile";
    public final static String MaxDepartmentLevel_key = "max.dept.level";
    public Long defaultLogonDept(long tenantId) {
        Number number = settingService.get(tenantId, Setting_Module_UserLogon, DefaultDept_Key, Long.class);
        if (number == null) return null;
        return number.longValue();
    }
    public void setDefaultLogonDept(long tenantId, Long deptId, String deptName) {
        settingService.save(tenantId, Setting_Module_UserLogon, DefaultDept_Key, String.valueOf(deptId), deptName);
    }
    public boolean isLogonRequireMobile(long tenantId) {
        return settingService.get(tenantId, Setting_Module, DefaultDept_Key, false);
    }
    public int getMaxLevel(Long tenatnId){
        Number number = settingService.get(tenatnId, Setting_Module, MaxDepartmentLevel_key, Integer.class);
        if (number == null) return 0;
        else return number.intValue();
    }


    private final static byte[] charIndex = new byte[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private final static int[] charValue = new int[256];
    private final static int charCount = charIndex.length;
    private final static int maxValue = charCount * charCount;
    private static void setCharValue(byte[] chars) {
        for (int i = 0; i < chars.length; i++) {
            charValue[chars[i]] = i;
        }
    }
    private static int charValue(byte key) {
        return charValue[key];
    }
    static {
        setCharValue(charIndex);
    }
    private static int charCouple2Decimal(byte[] bytes, int index) {
        if (bytes.length < index + 2) throw new RuntimeException("Invalid bytes.");
        int value = charValue(bytes[index]);
        value *= charCount;
        value += charValue(bytes[index + 1]);
        return value;
    }
    private static void decimal2CharCouple(int value, byte[] bytes, int index) {
        if (bytes.length < index + 2) throw new RuntimeException("Invalid bytes.");
        if (value >= maxValue) throw new RuntimeException("Invalid value.");
        int low = value % charCount;
        int high = value / charCount;
        bytes[index] = charIndex[high];
        bytes[index + 1] = charIndex[low];
    }
    private static String decimal2CharCouple(int value, String parentCode, int index) {
        byte[] result = new byte[2];
        decimal2CharCouple(value, result, 0);
        String childCode = new String(result);
        return parentCode + childCode;
    }

    public List<Department> childrenOf(long tenantId, long parentId) {
        return departmentCache.childrenOf(tenantId, parentId);
    }

    public String nextCode(long tenantId, long parentId) {
        String parentCode = "";
        if (parentId != 0) {
            Department parent = departmentRepository.findOneByTenantIdAndId(tenantId, parentId);
            if (parent == null) throw new BusinessException("上级部门不存在！");
            parentCode = parent.getCode();
        }
        Department prevDept = departmentRepository.findFirstByTenantIdAndParentIdOrderByCodeDesc(tenantId, parentId);
        int nextCode = 0;
        int index = parentCode.length();
        if (prevDept != null) {
            nextCode = charCouple2Decimal(prevDept.getCode().getBytes(), index) + 1;
            if (nextCode == maxValue) {
                List<Department> departments = departmentRepository.findByTenantIdAndParentIdOrderByCode(tenantId, parentId);
                int prevCode = -1;
                for (Department department : departments) {
                    int code = charCouple2Decimal(department.getCode().getBytes(), index);
                    if (code - prevCode != 1) {
                        nextCode = code - 1;
                        break;
                    }
                }
            }
        }
        String code = decimal2CharCouple(nextCode, parentCode, index);
        return code;
    }

    public Department findByTenantIdAndName(Long tid, String deptName) {
        return departmentRepository.findByNameAndTenantId(deptName, tid);
    }
}
