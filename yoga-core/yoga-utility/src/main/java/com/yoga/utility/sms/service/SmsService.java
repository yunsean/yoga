package com.yoga.utility.sms.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.spring.SpringContext;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.model.Setting;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.sms.mapper.SmsResultMapper;
import com.yoga.utility.sms.model.SmsResult;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
@EnableAsync
public class SmsService extends BaseService {
	public final static String ModuleName = "gcf_sms";
	public final static String Key_SmsConfig = "sms.setting";

	@Autowired
	private SpringContext springContext;
	@Autowired
	private SettingService settingService;
	@Autowired
	private SmsResultMapper smsResultMapper;

	List<Class<?>> smsServices = new ArrayList<>();

	@Bean
	public AsyncTaskExecutor smsTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("Sms-Executor");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(1000);
		executor.initialize();
		return executor;
	}

	@PostConstruct
	public void loadSmsService() {
		log.debug("查找短信发布服务");
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(SmsFactory.class));
		Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("com.yoga.**");
		for (BeanDefinition beanDefinition : beanDefinitionSet) {
			try {
				Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
				smsServices.add(entityClass);
				log.debug("找到短信发布服务：" + entityClass.getSimpleName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		log.debug("共找到" + smsServices.size() + "个短信发布服务");
	}

	public Setting getSetting(long tenantId) {
		Setting setting = settingService.get(tenantId, ModuleName, Key_SmsConfig);
		return setting;
	}
	public void setSetting(long tenantId, String value, String showValue) {
		settingService.save(tenantId, ModuleName, Key_SmsConfig, value, showValue);
	}

	@Async("smsTaskExecutor")
	public void sendSms(long tenantId, String mobile, String content, String action) {
		SmsActor actor = getActor(tenantId);
		sendSms(tenantId, actor, mobile, content, action);
	}
	@Async("smsTaskExecutor")
	public void sendSms(long tenantId, SmsActor actor, String mobile, String content, String action) {
		if (actor == null) return;
		String result = actor.sendSms(mobile, content);
		boolean isOk = StringUtil.isBlank(result);
		SmsResult smsResult = new SmsResult(tenantId, mobile, action, content, isOk, result);
		smsResultMapper.insert(smsResult);
	}

	public void sendSmsSync(long tenantId, String mobile, String content, String action) {
		SmsActor actor = getActor(tenantId);
		sendSmsSync(tenantId, actor, mobile, content, action);
	}
	public void sendSmsSync(long tenantId, SmsActor actor, String mobile, String content, String action) {
		if (actor == null) return;
		String result = actor.sendSms(mobile, content);
		boolean isOk = StringUtil.isBlank(result);
		SmsResult smsResult = new SmsResult(tenantId, mobile, action, content, isOk, result);
		smsResultMapper.insert(smsResult);
		if (!isOk) throw new BusinessException(result);
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
		if (StringUtil.isBlank(service)) throw new BusinessException("短信网关配置无效");
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
