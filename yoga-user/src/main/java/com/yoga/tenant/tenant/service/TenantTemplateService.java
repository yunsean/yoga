package com.yoga.tenant.tenant.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.model.SaveItem;
import com.yoga.tenant.tenant.model.TemplateSetting;
import com.yoga.tenant.tenant.model.TenantMenu;
import com.yoga.tenant.tenant.model.TenantTemplate;
import com.yoga.tenant.tenant.repo.TemplateSettingRepository;
import com.yoga.tenant.tenant.repo.TenantMenuRepository;
import com.yoga.tenant.tenant.repo.TenantTemplateRepository;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.sequence.SequenceNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TenantTemplateService extends BaseService {
	
	@Autowired
	private TenantTemplateRepository tenantTemplateRepository;
	@Autowired
	private TenantMenuRepository tenantMenuRepository;
	@Autowired
	private TemplateSettingRepository templateSettingRepository;

	private static String[] defaultModule = {
			"pri_role",
			"pri_dept",
			"pri_duty",
			"pri_user",
			"gbl_settable"
	};

	public void add(String name, String remark) throws Exception {
		if (StrUtil.isBlank(name)) throw new BusinessException("请输入模板名称");
		TenantTemplate template = new TenantTemplate(name, remark);
		template.setModules(defaultModule);
		template.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_TENANT_ID));
		tenantTemplateRepository.save(template);
	}
	public void update(long id, String name, String remark){
		TenantTemplate saved = tenantTemplateRepository.findOne(id);
		if (saved == null) throw new BusinessException("未找到该模板！");
		if (StrUtil.isNotBlank(name)) saved.setName(name);
		if (StrUtil.isNotBlank(remark)) saved.setRemark(remark);
		tenantTemplateRepository.save(saved);
	}

	public void delete(long id) {
		TenantTemplate tenant = tenantTemplateRepository.findOne(id);
		if (tenant == null) throw new BusinessException("未找到该模板");
		tenantTemplateRepository.delete(id);
	}

	public PageList<TenantTemplate> list(String name, int pageIndex, int pageSize){
		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		PageRequest request = new PageRequest(pageIndex, pageSize, sort);
		return new PageList<TenantTemplate>(tenantTemplateRepository.findAll((Root<TenantTemplate> root, CriteriaQuery<?> query, CriteriaBuilder cb)-> {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				if (name != null) expressions.add(cb.like(root.get("name"), "%" + name + "%"));
				return predicate;
		}, request));
	}

	public String[] modules(long tenantId) {
		TenantTemplate tenant = tenantTemplateRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该模板");
        return tenant.getModules();
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveModules(long tenantId, String[] modules) throws Exception {
		TenantTemplate tenant = tenantTemplateRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该模板");
        tenant.setModules(modules);
		tenantTemplateRepository.save(tenant);
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
        TenantTemplate tenant = tenantTemplateRepository.findOne(tenantId);
        if (tenant == null) throw new BusinessException("未找到该模板");
        TenantMenu tenantMenu = new TenantMenu(tenantId, code, group, name, url, remark, sort);
        tenantMenu.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_TENANT_MENU_ID));
        tenantMenuRepository.save(tenantMenu);
    }
    public void delMenu(long menuId) {
		TenantMenu menu = tenantMenuRepository.findOne(menuId);
		if (menu == null) throw new BusinessException("未找到菜单项！");
        tenantMenuRepository.deleteById(menuId);
    }
	@Transactional(propagation = Propagation.REQUIRED)
    public void updateMenu(long menuId, String code, String group, String name, String url, String remark, Integer sort) throws Exception {
        TenantMenu saved = tenantMenuRepository.findOne(menuId);
        if (saved == null) throw new BusinessException("未找到菜单项！");
        if (StrUtil.isBlank(name)) name = saved.getName();
		if (StrUtil.isNotBlank(code)) saved.setCode(code);
		if (StrUtil.isNotBlank(group)) saved.setGroup(group);
		if (StrUtil.isNotBlank(url) && !url.equals(saved.getUrl()))saved.setUrl(url);
		saved.setName(name);
		if (StrUtil.isNotBlank(remark)) saved.setRemark(remark);
		if (sort != null) saved.setSort(sort);
        tenantMenuRepository.save(saved);
    }

	public void saveSetting(long templateId, String module, String key, String value, String showValue) {
		TemplateSetting setting = templateSettingRepository.findOneByTemplateIdAndModuleAndKey(templateId, module, key);
		if (setting == null) {
			setting = new TemplateSetting();
			setting.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_SETTING_ID));
			setting.setTemplateId(templateId);
			setting.setModule(module);
			setting.setKey(key);
		}
		setting.setValue(value);
		setting.setShowValue(showValue);
		templateSettingRepository.save(setting);
	}
	@Transactional
	public void save(long templateId, List<SaveItem> items) {
		for (SaveItem item : items) {
			saveSetting(templateId, item.getModule(), item.getKey(), item.getValue(), item.getValue());
		}
	}
}
