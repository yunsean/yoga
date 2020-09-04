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
import com.yoga.tenant.menu.MenuItem;
import com.yoga.tenant.tenant.cache.TenantCache;
import com.yoga.tenant.tenant.mapper.TenantMapper;
import com.yoga.tenant.tenant.mapper.TenantMenuMapper;
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
@LoggingPrimary(module = TemplateService.ModuleName, name = "租户模板")
public class TemplateService extends BaseService implements LoggingPrimaryHandler {

	@Autowired
	private TenantMapper tenantMapper;
	@Autowired
	private TenantMenuMapper tenantMenuMapper;
	@Autowired
	private SettingService settingService;
	@Autowired
	private TenantChangedObserver changedObserver;

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

	@Transactional
	@Logging(module = ModuleName, description = "添加租户模板", primaryKeyIndex = -1, argNames = "租户名称，租户描述")
	public long add(String name, String remark) throws Exception {
		Tenant tenant = new Tenant(name, remark);
        tenantMapper.insert(tenant);
		return tenant.getId();
	}
	@Logging(module = ModuleName, description = "修改租户模板信息", primaryKeyIndex = 0, argNames = "租户模板ID，租户名称，租户描述")
	public void update(long id, String name, String remark) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户模板");
		if (name != null) tenant.setName(name);
		if (remark != null) tenant.setRemark(remark);
		tenantMapper.updateByPrimaryKey(tenant);
	}
	@Transactional
	@Logging(module = ModuleName, description = "删除租户模板", primaryKeyIndex = 0)
	public void delete(long id) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户模板");
		tenant.setDeleted(true);
		tenantMapper.updateByPrimaryKey(tenant);
		changedObserver.onDeleted(id);
	}
	public PageInfo<Tenant> list(String name, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex + 1, pageSize);
		List<Tenant> tenants = new MapperQuery<>(Tenant.class)
				.andEqualTo("template", true)
				.andLike("name", "%" + name + "%", StringUtil.isNotBlank(name))
				.query(tenantMapper);
		return new PageInfo<>(tenants);
	}
	public Tenant get(long id) {
		Tenant tenant = tenantMapper.selectByPrimaryKey(id);
		if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户模板");
		return tenant;
	}

	public String[] getModules(long tenantId) {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
		if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户模板");
        return tenant.listModules();
	}
	@Transactional
	@Logging(module = ModuleName, description = "设置租户模板模块", primaryKeyIndex = 0, argNames = "租户模板ID，租户模板模块列表")
	public void setModules(long tenantId, String[] modules) throws Exception {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
        if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户");
        tenant.saveModules(modules);
		tenantMapper.updateByPrimaryKey(tenant);
	}
	public List<MenuItem> getMenus(long tenantId) {
		List<TenantMenu> tenantMenus = new MapperQuery<>(TenantMenu.class)
				.andEqualTo("tenantId", tenantId)
				.query(tenantMenuMapper);
		if (tenantMenus == null || tenantMenus.isEmpty()) return null;
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
		if (tenantMenus == null || tenantMenus.isEmpty()) return null;
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
	@Logging(module = ModuleName, description = "添加租户模板菜单", primaryKeyIndex = 0, argNames = "租户模板ID，菜单编码，菜单分组，菜单名称，菜单URL，菜单描述，排序值")
    public void addMenu(long tenantId, String code, String group, String name, String url, String remark, int sort) throws Exception {
        Tenant tenant = tenantMapper.selectByPrimaryKey(tenantId);
        if (tenant == null || !tenant.getTemplate()) throw new BusinessException("未找到该租户模板");
        TenantMenu tenantMenu = new TenantMenu(tenantId, code, group, name, url, remark, sort);
        tenantMenuMapper.insert(tenantMenu);
		changedObserver.onMenuAdded(tenantId, name, url);
    }
    @Logging(module = ModuleName, description = "删除租户模板菜单", primaryKeyIndex = 0, argNames = "租户模板ID，菜单ID")
    public void deleteMenu(long tenantId, long menuId) {
		TenantMenu menu = tenantMenuMapper.selectByPrimaryKey(menuId);
		if (menu == null || menu.getTenantId() != tenantId) throw new BusinessException("未找到菜单项！");
		tenantMenuMapper.deleteByPrimaryKey(menuId);
		changedObserver.onMenuDeleted(menu.getTenantId(), menu.getName(), menu.getUrl());
    }
	@Transactional
	@Logging(module = ModuleName, description = "修改租户模板菜单", primaryKeyIndex = 0, argNames = "租户模板ID，菜单ID，菜单编码，菜单分组，菜单名称，菜单URL，菜单描述，排序值")
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

    @Logging(module = ModuleName, description = "修改租户模板基础设置", primaryKeyIndex = 0, argNames = "租户模板ID，基础设置")
	public void saveSetting(long tenantId, TenantSetting setting) {
		settingService.save(tenantId, ModuleName, Key_Setting, setting, setting.getPlatformName());
		redisOperator.removePattern("*com.yoga.tenant.tenant.service.TenantService.getSetting." + tenantId);
		redisOperator.removePattern("*com.yoga.tenant.tenant.service.TenantService.settingMap." + tenantId);
	}
	@Logging(module = ModuleName, description = "修改租户模板框架设置", primaryKeyIndex = 0, argNames = "租户模板ID，框架设置")
	public void saveCustomize(long tenantId, TenantCustomize customize) {
		settingService.save(tenantId, ModuleName, Key_Customize, customize, "已设置");
		redisOperator.removePattern("*com.yoga.tenant.tenant.service.TenantService.getCustomize." + tenantId);
	}
}
