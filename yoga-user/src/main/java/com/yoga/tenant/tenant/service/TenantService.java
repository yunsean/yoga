package com.yoga.tenant.tenant.service;

import com.alibaba.fastjson.JSON;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.core.utils.Utility;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.repo.SettingRepository;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.cache.TenantCache;
import com.yoga.tenant.tenant.dto.UpdateTenantDto;
import com.yoga.tenant.tenant.enums.IndexModeType;
import com.yoga.tenant.tenant.model.*;
import com.yoga.tenant.tenant.repo.TenantMenuRepository;
import com.yoga.tenant.tenant.repo.TenantRepository;
import com.yoga.tenant.tenant.repo.TenantTemplateRepository;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.role.model.Role;
import com.yoga.user.role.repository.RoleRepository;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.user.user.model.User;
import com.yoga.user.user.repo.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class TenantService extends BaseService {

    private static String[] defaultModule = {
            "pri_role",
            "pri_dept",
            "pri_duty",
			"pri_user",
			"pri_login",
            "gbl_settable"
    };
    private static String[] defaultPrivilege = {
			"pri_role",
			"pri_dept",
			"pri_duty",
			"pri_user",
			"gbl_settable",
			"pri_role.add",
			"pri_role.del",
			"pri_role.update",
			"pri_dept.add",
			"pri_dept.del",
			"pri_dept.update",
			"pri_duty.add",
			"pri_duty.del",
			"pri_duty.update",
			"pri_user.add",
			"pri_user.del",
			"pri_user.update",
			"pri_manage.login",
			"gbl_settable.query",
			"gbl_settable.add"
	};
	
	@Autowired
	private TenantRepository tenantRepository;
	@Autowired
	private TenantMenuRepository tenantMenuRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SettingService settingService;
	@Autowired
	private PermissionDAO permissionDAO;
	@Autowired
	private TenantChangedObserver changedObserver;
	@Autowired
	private TenantTemplateRepository templateRepository;
	@Autowired
	private SettingRepository settingRepository;
	@Autowired
	private TenantCache tenantCache;

	public Map<String, Long> allCode2Id() {
		return tenantCache.allCode2Id();
	}
	public Tenant get(long id) {
		return tenantCache.get(id);
	}
	public List<Tenant> getAll() {
		return tenantCache.getAll();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public long add(String name, String code, String remark, Long templateId, String username, String firstName, String lastName, String password, String phone) throws Exception {
		if (tenantRepository.getCountByCode(code) > 0) throw new BusinessException("已经存在该CODE");
		TenantTemplate template = null;
		if (templateId != null) {
			template = templateRepository.findOne(templateId);
			if (template == null) throw new BusinessException("租户模板不存在！");
		}
		Tenant tenant = new Tenant();
		tenant.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_TENANT_ID));
		tenant.setName(name);
		tenant.setCode(code);
		tenant.setOrginCode(code);
		tenant.setCreateTime(new Date());
		tenant.setRemark(remark);
		tenant.setTemplateId(templateId);
		if (template != null) tenant.setModules(template.getModules());
        else tenant.setModules(defaultModule);
		tenantRepository.save(tenant);

		Role role = new Role(tenant.getId(), "admin", "系统管理员", "系统管理员");
		role.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_ROLE_ID));
		roleRepository.save(role);
		permissionDAO.setRolePermissions(tenant.getId(), role.getId(), defaultPrivilege);

		String pwd = DigestUtils.md5Hex(password);
		User user = new User(tenant.getId(), 0L, username, pwd, firstName, lastName, "系统管理员");
		user.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_U_USER_ID));
		user.setPhone(phone);
		userRepository.save(user);
		permissionDAO.setUserRoles(tenant.getId(), user.getId(), new long[]{role.getId()});

		if (template != null) {
			List<TenantMenu> menuItems = tenantMenuRepository.findAllByTenantId(template.getId());
			if (menuItems != null) menuItems.stream().forEach(menu-> {
				TenantMenu tenantMenu = new TenantMenu(tenant.getId(), menu.getCode(), menu.getGroup(), menu.getName(), menu.getUrl(), menu.getRemark(), menu.getSort());
				tenantMenu.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_TENANT_MENU_ID));
				tenantMenuRepository.save(tenantMenu);
			});
			List<Setting> settings = settingRepository.findByTenantId(template.getId());
			if (settings != null) settings.stream().forEach(setting -> {
				Setting setting1 = new Setting(0, tenant.getId(), setting.getKey(), setting.getValue(), setting.getModule(), setting.getShowValue());
				setting1.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_SETTING_ID));
				settingRepository.save(setting1);
			});
		}

		changedObserver.onCreated(tenant.getId(), tenant.getName(), tenant.getCode());
		tenantCache.clearAll();
		return tenant.getId();
	}
	public void update(UpdateTenantDto updateTenant){
		Tenant tenant = tenantRepository.findOne(updateTenant.getId());
		if (tenant == null) throw new BusinessException("未找到该租户");
		tenant.setName(updateTenant.getName());
		tenant.setRemark(updateTenant.getRemark());
		tenantRepository.save(tenant);
		tenantCache.clearTenant(updateTenant.getId());
	}
	public void del(long id) {
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant == null) throw new BusinessException("未找到该租户");
		tenant.setDeleted(true);
		tenantRepository.save(tenant);
		changedObserver.onDeleted(id);
		tenantCache.clearAll();
		tenantCache.clearTenant(id);
	}

	public class RepairResult {
		private String username;
		private String password;
		private String notes;

		public String getUsername() {
			return username;
		}
		public String getPassword() {
			return password;
		}
		public String getNotes() {
			return notes;
		}
	}
	@Transactional
	public RepairResult repair(long id) {
		Tenant tenant = tenantRepository.findOne(id);
		if (tenant == null) throw new BusinessException("未找到该租户");
		for (int i = 0; i < 10000; i++) {
			String username = "admin" + i;
			User admin = userRepository.findFirstByTenantIdAndUsername(id, username);
			if (admin != null) continue;
			Role role = new Role(tenant.getId(), "_admin_", "应急系统管理员", "应急系统管理员");
			role.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_ROLE_ID));
			roleRepository.save(role);
			permissionDAO.setRolePermissions(tenant.getId(), role.getId(), defaultPrivilege);
			String pwd = Utility.randomCode(10);
			String password = DigestUtils.md5Hex(pwd);
			admin = new User(tenant.getId(), 0L, username, password, "管理员", "应急", "应急系统管理员");
			admin.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_U_USER_ID));
			userRepository.save(admin);
			permissionDAO.setUserRoles(id, admin.getId(), new long[]{role.getId()});
			RepairResult result = new RepairResult();
			result.username = username;
			result.password = pwd;
			result.notes = "创建应急管理员账号成功，请使用[" + username + "]和密码[" + pwd + "]登录。为了系统安全，请在恢复账号系统后自行删除[应急管理员]用户和[应急系统管理员]角色！";
			return result;
		}
		throw new BusinessException("创建应急管理员失败");
	}

	public PageList<Tenant> tenants(String name, String code, int pageIndex, int pageSize){
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		PageRequest request = new PageRequest(pageIndex, pageSize, sort);
		return new PageList<Tenant>(tenantRepository.findAll(new Specification<Tenant>() {
			@Override
			public Predicate toPredicate(Root<Tenant> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (name != null) expressions.add(cb.like(root.get("name"), "%" + name + "%"));
				if (code != null) expressions.add(cb.like(root.get("code"), "%" + code + "%"));
				expressions.add(cb.equal(root.get("deleted"), 0));
				return predicate;
			}
		}, request));
	}

	public String[] modules(long tenantId) {
        Tenant tenant = tenantRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该租户");
        return tenant.getModules();
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveModules(long tenantId, String[] modules) throws Exception {
        Tenant tenant = tenantRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该租户");
        tenant.setModules(modules);
        tenantRepository.save(tenant);
		changedObserver.onModuleChanged(tenantId, modules);
	}

	public List<MenuItem> menus(long tenantId) {
        Iterable<TenantMenu> tenantMenus = tenantMenuRepository.findAllByTenantId(tenantId);
        if (tenantMenus == null) return null;
        Map<String, List<MenuItem>> maps = new HashMap<>();
        for (TenantMenu tenantMenu : tenantMenus) {
            List<MenuItem> menus = maps.get(tenantMenu.getGroup());
            if (menus != null) menus.add(tenantMenu.asMenuItem());
            else maps.put(tenantMenu.getGroup(), new ArrayList<MenuItem>() {{
                add(tenantMenu.asMenuItem());
            }});
        }
        List<MenuItem> result = new ArrayList<>();
        for (String group : maps.keySet()) {
            MenuItem item = new MenuItem(group, maps.get(group));
            result.add(item);
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED)
    public void addMenu(long tenantId, String code, String group, String name, String url, String remark, int sort) throws Exception {
        Tenant tenant = tenantRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该租户");
        TenantMenu tenantMenu = new TenantMenu(tenantId, code, group, name, url, remark, sort);
        tenantMenu.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_TENANT_MENU_ID));
        tenantMenuRepository.save(tenantMenu);
		changedObserver.onMenuAdded(tenantId, name, url);
    }
    public void delMenu(long menuId) {
		TenantMenu menu = tenantMenuRepository.findOne(menuId);
		if (menu == null) throw new BusinessException("未找到菜单项！");
        tenantMenuRepository.deleteById(menuId);
		changedObserver.onMenuDeleted(menu.getId(), menu.getName(), menu.getUrl());
    }
	@Transactional(propagation = Propagation.REQUIRED)
    public void updateMenu(long menuId, String code, String group, String name, String url, String remark, Integer sort) throws Exception {
        TenantMenu saved = tenantMenuRepository.findOne(menuId);
        if (saved == null) throw new BusinessException("未找到菜单项！");
        if (StrUtil.isBlank(name)) name = saved.getName();
		if (StrUtil.isNotBlank(code)) saved.setCode(code);
		if (StrUtil.isNotBlank(group)) saved.setGroup(group);
		if (StrUtil.isNotBlank(url) && !url.equals(saved.getUrl())) {
			changedObserver.onMenuDeleted(saved.getId(), saved.getName(), saved.getUrl());
			changedObserver.onMenuAdded(saved.getId(), name, url);
			saved.setUrl(url);
		}
		saved.setName(name);
		if (StrUtil.isNotBlank(remark)) saved.setRemark(remark);
		if (sort != null) saved.setSort(sort);
        tenantMenuRepository.save(saved);
    }

    public final static String Setting_Module = "gbl_tenant";
	public final static String Setting_Key = "tenantSetting";
	public final static String Customize_Key = "tenantCustomize";
	public final static String IndexMode_Key = "tenantIndexMode";
	public IndexModeType indexModeType(long tenantId) {
		Setting setting = settingService.get(tenantId, Setting_Module, IndexMode_Key);
		if (setting == null) return IndexModeType.Admin;
		String type = setting.getValue();
		if (type == null) return IndexModeType.Admin;
		else if (type.equals(IndexModeType.Web.toString())) return IndexModeType.Web;
		else if (type.equals(IndexModeType.Auto.toString())) return IndexModeType.Auto;
		else return IndexModeType.Admin;
	}

	public void saveSetting(long tenantId, TenantSetting setting) {
		String json = JSON.toJSONString(setting);
		settingService.save(tenantId, Setting_Module, Setting_Key, json, setting.getPlatformName());
		redisOperator.remove("com.yoga.tenant.tenant.service.TenantService.getSetting." + tenantId);
		redisOperator.remove("com.yoga.tenant.tenant.service.TenantService.settingMap." + tenantId);
	}
	public void saveCustomize(long tenantId, TenantCustomize customize) {
		String json = JSON.toJSONString(customize);
		settingService.save(tenantId, Setting_Module, Customize_Key, json, "已设置");
		redisOperator.remove("com.yoga.tenant.tenant.service.TenantService.getCustomize." + tenantId);
	}
	@Cacheable(value = "getSetting", keyGenerator = "wiselyKeyGenerator")
	public TenantSetting getSetting(long tenantId) {
		Setting setting = settingService.get(tenantId, Setting_Module, Setting_Key);
		TenantSetting tenantSetting = null;
		if (setting != null) {
			try {
				tenantSetting = JSON.parseObject(setting.getValue(), TenantSetting.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (tenantSetting == null) {
			tenantSetting = new TenantSetting();
			tenantSetting.setPlatformName("YOGA管理平台");
			tenantSetting.setFooterRemark("Copyright @ 2017");
			tenantSetting.setResourcePrefix("");
			tenantSetting.setLoginBackUrl("/login/back.jpg");
			tenantSetting.setLoginLogoUrl("/login/pick.png");
			tenantSetting.setTopImageUrl("/login/top.png");
		}
		return tenantSetting;
	}
	@Cacheable(value = "getCustomize", keyGenerator = "wiselyKeyGenerator")
	public TenantCustomize getCustomize(long tenantId) {
		Setting setting = settingService.get(tenantId, Setting_Module, Customize_Key);
		TenantCustomize customize = null;
		if (setting != null) {
			try {
				customize = JSON.parseObject(setting.getValue(), TenantCustomize.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (customize == null) customize = new TenantCustomize();
		return customize;
	}
	@Cacheable(value = "settingMap", keyGenerator = "wiselyKeyGenerator")
	public Map<String, Object> settingMap(long tenantId) {
		TenantSetting setting = getSetting(tenantId);
		Map<String, Object> map = new HashMap<>();
		if (setting != null) {
			map.put("platform", setting.getPlatformName());
			map.put("footer", setting.getFooterRemark());
			map.put("resource", setting.getResourcePrefix());
			map.put("role", setting.getRoleAlias());
			map.put("dept", setting.getDeptAlias());
			map.put("duty", setting.getDutyAlias());
		}
		return map;
	}

    public void ensureSystemTenant() {
		Tenant tenant = tenantRepository.findOne(0L);
		if (tenant == null) {
			tenant = new Tenant();
			tenant.setId(0L);
			tenant.setName("系统站点");
			tenant.setRemark("系统基础站点，请勿删除");
			tenant.setCode("system");
			tenant.setCreateTime(new Date());
			tenant.setModules(defaultModule);
			tenant.setDeleted(false);
			tenant.setOrginCode("system");
			tenantRepository.save(tenant);
		}
	}
}
