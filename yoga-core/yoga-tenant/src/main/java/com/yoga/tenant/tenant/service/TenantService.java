package com.yoga.tenant.tenant.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.operator.role.service.RoleService;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.setting.mapper.SettingMapper;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import com.yoga.tenant.tenant.cache.TenantCache;
import com.yoga.tenant.tenant.mapper.TenantMapper;
import com.yoga.tenant.tenant.mapper.TenantMenuMapper;
import com.yoga.tenant.menu.MenuItem;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantMenu;
import com.yoga.tenant.tenant.model.TenantSetting;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@LoggingPrimary(module = TenantService.ModuleName, name = "租户管理")
public class TenantService extends BaseService implements LoggingPrimaryHandler {

	@Autowired
	private TenantMapper tenantMapper;
	@Autowired
	private TenantMenuMapper tenantMenuMapper;
	@Autowired
	private SettingMapper settingMapper;
	@Autowired
	private SettingService settingService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private TenantChangedObserver changedObserver;
	@Autowired
	private TenantCache tenantCache;

	@Value("${app.login.default-back:/admin/login/back.jpg}")
	private String defaultBack = "/admin/login/back.jpg";
	@Value("${app.login.default-pick:/admin/login/pick.png}")
	private String defaultPick = "/admin/login/pick.png";
	@Value("${app.login.default-top:/admin/login/top.png}")
	private String defaultTop = "/admin/login/top.png";
	@Value("${app.login.default-title:YOGA管理平台}")
	private String defaultTitle = "YOGA管理平台";
	@Value("${app.login.default-footer:Copyright @ 2017}")
	private String defaultFooter = "Copyright @ 2017";

	public final static String ModuleName = "gbl_tenant";
	public final static String Key_Setting = "tenant.setting";
	public final static String Key_Customize = "tenant.customize";
	@Override
	public String getPrimaryInfo(Object primaryId) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(primaryId);
		if (tenant == null) return null;
		return tenant.getName();
	}

	private static String[] defaultModule = {
			"admin_role",
			"admin_branch",
			"admin_duty",
			"admin_user",
			"sys_config"
	};
	private static String[] defaultPrivilege = {
			"admin_role",
			"admin_role.add",
			"admin_role.del",
			"admin_role.update",

			"admin_branch",
			"admin_branch.add",
			"admin_branch.del",
			"admin_branch.update",

			"admin_duty",
			"admin_duty.add",
			"admin_duty.del",
			"admin_duty.update",

			"admin_user",
			"admin_user.add",
			"admin_user.del",
			"admin_user.update",

			"admin.login",

			"sys_config",
			"sys_config.update"
	};

	public Map<String, Long> allCode2Id() {
		return tenantCache.allCode2Id();
	}
	public Tenant get(long id) {
		return tenantCache.get(id);
	}
	public List<Tenant> getAll() {
		return tenantCache.getAll();
	}

	@Transactional
	@Logging(module = ModuleName, description = "添加租户", primaryKeyIndex = -1, excludeArgs = 5, argNames = "租户名称，租户编码，租户描述，模板ID，管理员账号，，管理员昵称，管理员手机号")
	public long add(String name, String code, String remark, Long templateId, String username, String password, String nickname, String mobile) throws Exception {
		if (StringUtil.isNotBlank(code)) {
			if (new MapperQuery<>(Tenant.class)
					.andEqualTo("template", false)
					.andEqualTo("code", code)
					.count(tenantMapper) > 0) throw new BusinessException("已经存在该CODE");
		}
		Tenant template = null;
		if (templateId != null) {
			template = tenantMapper.selectByPrimaryKey(templateId);
			if (template == null || !template.getTemplate()) throw new BusinessException("租户模板不存在！");
		}
		Tenant tenant = new Tenant(name, code, remark, templateId, null);
		if (template != null) tenant.saveModules(template.listModules());
        tenantMapper.insert(tenant);

		if (template != null) {
			List<TenantMenu> menuItems = new MapperQuery<>(TenantMenu.class)
					.andEqualTo("tenantId", templateId)
					.query(tenantMenuMapper);
			if (menuItems != null) menuItems.forEach(menu-> {
				TenantMenu tenantMenu = new TenantMenu(tenant.getId(), menu.getCode(), menu.getGroup(), menu.getName(), menu.getUrl(), menu.getRemark(), menu.getSort());
				tenantMenuMapper.insert(tenantMenu);
			});
			List<Setting> settings = new MapperQuery<>(Setting.class)
					.andEqualTo("tenantId", templateId)
					.query(settingMapper);
			settings.forEach(setting -> settingMapper.insert(new Setting(tenant.getId(), setting.getModule(), setting.getKey(), setting.getValue(), setting.getShowValue())));
		}

		long roleId = roleService.add(tenant.getId(), "系统管理员", "系统管理员");
		roleService.savePrivilege(tenant.getId(), roleId, defaultPrivilege);
		userService.add(tenant.getId(), username, password, null, null, nickname, null, null, null, mobile,
				null, null, null, null, null, new Long[]{roleId});

		changedObserver.onCreated(tenant.getId(), tenant.getName(), tenant.getCode());
		tenantCache.clearAll();
		return tenant.getId();
	}
	@Logging(module = ModuleName, description = "修改租户信息", primaryKeyIndex = 0, argNames = "租户ID，租户名称，租户描述")
	public void update(long id, String name, String remark) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
		if (name != null) tenant.setName(name);
		if (remark != null) tenant.setRemark(remark);
		tenantMapper.updateByPrimaryKey(tenant);
		tenantCache.clearTenant(id);
	}
	@Transactional
	@Logging(module = ModuleName, description = "删除租户", primaryKeyIndex = 0)
	public void delete(long id) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
		if ("system".equals(tenant.getCode())) throw new BusinessException("不允许删除系统租户");
		tenant.setOrginCode(tenant.getCode());
		tenant.setCode("");
		tenant.setDeleted(true);
		tenantMapper.updateByPrimaryKey(tenant);
		changedObserver.onDeleted(id);
		tenantCache.clearAll();
		tenantCache.clearTenant(id);
	}
	@Transactional
	@Logging(module = ModuleName, description = "恢复租户", primaryKeyIndex = 0, argNames = "租户ID，新租户编码")
	public void renew(long id, String code) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
		if (StringUtil.isNotBlank(code)) {
			if (new MapperQuery<>(Tenant.class)
					.andEqualTo("template", false)
					.andEqualTo("code", code)
					.count(tenantMapper) > 0) throw new BusinessException("已经存在该CODE");
		}
		tenant.setDeleted(false);
		tenant.setCode(code);
		tenantMapper.updateByPrimaryKey(tenant);
		changedObserver.onRenew(id);
		tenantCache.clearAll();
		tenantCache.clearTenant(id);
	}
	public List<Tenant> listAll() {
		return tenantMapper.selectAll();
	}
	public PageInfo<Tenant> list(String name, String code, Long templateId, Boolean deleted, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex + 1, pageSize);
		List<Tenant> tenants = new MapperQuery<>(Tenant.class)
				.andEqualTo("template", false)
				.andLike("name", "%" + name + "%", StringUtil.isNotBlank(name))
				.andEqualTo("code", code, StringUtil.isNotBlank(code))
				.andEqualTo("templateId", templateId, templateId != null && templateId != 0L)
				.andEqualTo("deleted", deleted, deleted != null)
				.orderBy("createTime", true)
				.query(tenantMapper);
		return new PageInfo<>(tenants);
	}

	@Transactional
	@Logging(module = ModuleName, description = "租户权限修复", primaryKeyIndex = 0)
	public String repair(long id) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
		for (int i = 0; i < 10000; i++) {
			String username = "admin" + i;
			User admin = userService.get(id, username);
			if (admin != null) continue;
			long roleId = roleService.add(id, "应急系统管理员", "应急系统管理员");
			roleService.savePrivilege(id, roleId, defaultPrivilege);
			String pwd = randomCode(10);
			String password = DigestUtils.md5Hex(pwd);
			userService.add(id, username, password, null, null, "应急管理员", null, null, null, null,
					null, null, null, null, null, new Long[]{roleId});
			return "创建应急管理员账号成功，请使用[" + username + "]和密码[" + pwd + "]登录。为了系统安全，请在恢复账号系统后自行删除[应急管理员]用户和[应急系统管理员]角色！";
		}
		throw new BusinessException("创建应急管理员失败");
	}
	private static String randomCode(int length){
		return  "" + ((long)((Math.random() * 9 + 1) * Math.pow(10, length - 1)));
	}

	public String[] getModules(long tenantId) {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
		if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
        return tenant.listModules();
	}
	@Transactional
	@Logging(module = ModuleName, description = "设置租户模块", primaryKeyIndex = 0, argNames = "租户ID，租户模块列表")
	public void setModules(long tenantId, String[] modules) throws Exception {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
        if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
        tenant.saveModules(modules);
		tenantMapper.updateByPrimaryKey(tenant);
		changedObserver.onModuleChanged(tenantId, modules);
	}

	public List<MenuItem> getMenus(long tenantId) {
		List<TenantMenu> tenantMenus = new MapperQuery<>(TenantMenu.class)
				.andEqualTo("tenantId", tenantId)
				.query(tenantMenuMapper);
		if (tenantMenus == null || tenantMenus.isEmpty()) return new ArrayList<>();
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
	public List<MenuItem> getMenus(long tenantId, long userId) {
		List<TenantMenu> tenantMenus = tenantMenuMapper.getMenusForUser(tenantId, userId);
		if (tenantMenus == null || tenantMenus.isEmpty()) return new ArrayList<>();
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
	public TenantMenu getMenu(long tenantId, long menuId) {
		TenantMenu menu = tenantMenuMapper.selectByPrimaryKey(menuId);
		if (menu == null || menu.getTenantId() != tenantId) throw new BusinessException("未找到菜单项！");
		return menu;
	}
	@Transactional
	@Logging(module = ModuleName, description = "添加租户菜单", primaryKeyIndex = 0, argNames = "租户ID，菜单编码，菜单分组，菜单名称，菜单URL，菜单描述，排序值")
    public void addMenu(long tenantId, String code, String group, String name, String url, String remark, int sort) throws Exception {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
        if (tenant == null || tenant.getTemplate()) throw new BusinessException("未找到该租户");
        TenantMenu tenantMenu = new TenantMenu(tenantId, code, group, name, url, remark, sort);
        tenantMenuMapper.insert(tenantMenu);
		changedObserver.onMenuAdded(tenantId, name, url);
    }
    @Logging(module = ModuleName, description = "删除租户菜单", primaryKeyIndex = 0, argNames = "租户ID，菜单ID")
    public void deleteMenu(long tenantId, long menuId) {
		TenantMenu menu = tenantMenuMapper.selectByPrimaryKey(menuId);
		if (menu == null || menu.getTenantId() != tenantId) throw new BusinessException("未找到菜单项！");
		tenantMenuMapper.deleteByPrimaryKey(menuId);
		changedObserver.onMenuDeleted(menu.getTenantId(), menu.getName(), menu.getUrl());
    }
	@Transactional
	@Logging(module = ModuleName, description = "修改租户菜单", primaryKeyIndex = 0, argNames = "租户ID，菜单ID，菜单编码，菜单分组，菜单名称，菜单URL，菜单描述，排序值")
    public void updateMenu(long tenantId, long menuId, String code, String group, String name, String url, String remark, Integer sort) throws Exception {
        TenantMenu saved = tenantMenuMapper.selectByPrimaryKey(menuId);
		if (saved == null || saved.getTenantId() != tenantId) throw new BusinessException("未找到菜单项！");
		if (StringUtil.isNotBlank(name)) saved.setName(name);
		if (StringUtil.isNotBlank(code)) saved.setCode(code);
		if (StringUtil.isNotBlank(group)) saved.setGroup(group);
		name = saved.getName();
		if (StringUtil.isNotBlank(url) && !url.equals(saved.getUrl())) {
			changedObserver.onMenuDeleted(saved.getId(), saved.getName(), saved.getUrl());
			changedObserver.onMenuAdded(saved.getId(), name, url);
			saved.setUrl(url);
		}
		if (StringUtil.isNotBlank(remark)) saved.setRemark(remark);
		if (sort != null) saved.setSort(sort);
		tenantMenuMapper.updateByPrimaryKey(saved);
    }

    @Logging(module = ModuleName, description = "修改租户基础设置", primaryKeyIndex = 0, argNames = "租户ID，基础设置")
	public void saveSetting(long tenantId, TenantSetting setting) {
		settingService.save(tenantId, ModuleName, Key_Setting, setting, setting.getPlatformName());
		redisOperator.remove("tenant::readSetting:" + tenantId);
	}
	@Logging(module = ModuleName, description = "修改租户框架设置", primaryKeyIndex = 0, argNames = "租户ID，框架设置")
	public void saveCustomize(long tenantId, TenantCustomize customize) {
		settingService.save(tenantId, ModuleName, Key_Customize, customize, "已设置");
		redisOperator.remove("tenant::readCustomize:" + tenantId);
	}
	@Cacheable(value = "tenant", keyGenerator = "wiselyKeyGenerator")
	public TenantSetting readSetting(long tenantId) {
		TenantSetting setting = settingService.get(tenantId, ModuleName, Key_Setting, TenantSetting.class);
		if (setting == null) {
			setting = new TenantSetting();
			setting.setPlatformName(defaultTitle);
			setting.setFooterRemark(defaultFooter);
			setting.setAdminIcon("/admin/login/favicon.ico");
			setting.setResourcePrefix("/admin");
			setting.setLoginBackUrl(defaultBack);
			setting.setLoginLogoUrl(defaultPick);
			setting.setTopImageUrl(defaultTop);
		}
		return setting;
	}
	@Cacheable(value = "tenant", keyGenerator = "wiselyKeyGenerator")
	public TenantCustomize readCustomize(long tenantId) {
		TenantCustomize customize = settingService.get(tenantId, ModuleName, Key_Customize, TenantCustomize.class);
		if (customize == null) customize = new TenantCustomize();
		return customize;
	}

	@PostConstruct
	public void ensureSystemTenant() {
		Tenant tenant = new MapperQuery<>(Tenant.class)
				.andEqualTo("code", "system")
				.queryFirst(tenantMapper);
		if (tenant != null && tenant.getId() == 0L) return;
		if (tenant == null) {
			tenant = new Tenant("系统站点", "system", "系统基础站点，请勿删除", null, null);
			tenant.saveModules(defaultModule);
			tenantMapper.insert(tenant);
		}
		tenantMapper.updateSystemTenantId();
	}
}
