package com.yoga.tenant.sms;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yoga.core.interfaces.sms.SmsActor;
import com.yoga.core.interfaces.sms.SmsFactory;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.sms.model.SmsHistory;
import com.yoga.tenant.sms.repo.SmsHistoryRepository;
import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class SmsService extends BaseService {
	public static final Logger logger = Logger.getLogger(SmsService.class);

	@Autowired
	private SpringContext springContext;
	@Autowired
	private SettingService settingService;
	@Autowired
	private SmsHistoryRepository historyRepository;

	List<Class<?>> smsServices = new ArrayList<>();

	@PostConstruct
	public void loadSmsService() {
		logger.info("查找短信发布服务");
		ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
		scan.addIncludeFilter(new AssignableTypeFilter(SmsFactory.class));
		Set<BeanDefinition> beanDefinitionSet = scan.findCandidateComponents("com.yoga.**");
		for (BeanDefinition beanDefinition : beanDefinitionSet) {
			try {
				Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
				smsServices.add(entityClass);
				logger.info("找到短信发布服务：" + entityClass.getSimpleName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		logger.info("共找到" + smsServices.size() + "个短信发布服务");
	}

	final static String SmsMoudle = "gcf_sms_notify";
	final static String SmsConfig = "smsConfig";
	public Setting getSetting(long tenantId) {
		Setting setting = settingService.get(tenantId, SmsMoudle, SmsConfig);
		return setting;
	}
	public void setSetting(long tenantId, String value, String showValue) {
		settingService.save(tenantId, SmsMoudle, SmsConfig, value, showValue);
	}
	public void sendSms(long tenantId, String mobile, String content, String action) {
		SmsActor actor = getActor(tenantId);
		sendSms(tenantId, actor, mobile, content, action);
	}
	public void sendSms(long tenantId, SmsActor actor, String mobile, String content, String action) {
		String result = actor.sendSms(mobile, content);
		if (!StrUtil.isBlank(result)) throw new BusinessException(result);
		SmsHistory history = new SmsHistory(tenantId, new Date(), mobile, action);
		history.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_SMS_HISTORY_ID));
		historyRepository.save(history);
	}
	public SmsActor getActor(long tenantId) {
		Setting setting = getSetting(tenantId);
		if (setting == null) throw new BusinessException("短信网关尚未配置");
		JSONObject jsonObject = JSON.parseObject(setting.getValue());
		String service = null;
		Map<String, String> configs = new HashMap<>();
		for (String key : jsonObject.keySet()) {
			if (key.equals("service")) service = jsonObject.getString(key);
			else configs.put(key, jsonObject.getString(key));
		}
		if (StrUtil.isBlank(service)) throw new BusinessException("短信网关配置无效");
		SmsFactory smsFactory = (SmsFactory) springContext.getApplicationContext().getBean(service);
		if (smsFactory == null) throw new BusinessException("无法连接短信网关");
		SmsActor actor = smsFactory.build(configs);
		if (actor == null) throw new BusinessException("无法访问短信网关");
		return actor;
	}

	public Map<String, String> services() {
		Map<String, String> names = new HashMap<>();
		for (Class<?> service : smsServices) {
			SmsFactory smsFactory = (SmsFactory) springContext.getApplicationContext().getBean(service);
			String[] name = springContext.getApplicationContext().getBeanNamesForType(service);
			if (name == null || name.length < 1) continue;
			names.put(name[0], smsFactory.getName());
		}
		return names;
	}
	public Map<String, String> configItems(String service) {
		try {
			SmsFactory smsFactory = (SmsFactory) springContext.getApplicationContext().getBean(service);
			return smsFactory.configItems();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getServiceName(String service) {
		try {
			SmsFactory smsFactory = (SmsFactory) springContext.getApplicationContext().getBean(service);
			return smsFactory.getName();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
