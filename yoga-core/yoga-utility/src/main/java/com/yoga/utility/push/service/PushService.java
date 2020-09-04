package com.yoga.utility.push.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.spring.SpringContext;
import com.yoga.utility.push.mapper.PushUserMapper;
import com.yoga.utility.push.model.PushUser;
import com.yoga.utility.sms.service.SmsActor;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@EnableAsync
public class PushService extends BaseService {
	public final static String ModuleName = "gcf_push";

	private static final Logger logger = LoggerFactory.getLogger(PushService.class);
	private List<PushActor> pushActors = new ArrayList<>();
	private Map<String, PushActor> pushActorMap = new HashMap<>();

	@Autowired
	private SpringContext springContext;
	@Autowired
	private PushUserMapper pushUserMapper;

	@Bean
	public AsyncTaskExecutor pushTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("Push-Executor");
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(1000);
		executor.initialize();
		return executor;
	}

	@PostConstruct
	public void loadPushService() {
		logger.info("查找信息推送服务");
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(PushActor.class));
		Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("com.yoga.**");
		for (BeanDefinition beanDefinition : beanDefinitionSet) {
			try {
				Class<?> entityClass = ClassUtils.getClass(beanDefinition.getBeanClassName());
				PushActor actor = (PushActor) springContext.getApplicationContext().getBean(entityClass);
				pushActors.add(actor);
				pushActorMap.put(actor.getCode(), actor);
				logger.info("找到信息推送服务：" + entityClass.getSimpleName());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		pushActors.sort(Comparator.comparing(PushActor::getPriority));
		logger.info("共找到" + pushActors.size() + "个信息推送服务");
	}

	@Async("pushTaskExecutor")
	public void pushSingle(long tenantId, long userId, String title, String content, Map<String, String> extras) {
		PushUser pushUser = pushUserMapper.selectByPrimaryKey(userId);
		if (pushUser == null) return;
		PushActor actor = pushActorMap.get(pushUser.getPushChannel());
		if (actor == null) return;
		actor.pushToSingle(tenantId, pushUser.getClientId(), title, content, null, extras);
	}
	@Async("pushTaskExecutor")
	public void pushBroadcast(long tenantId, List<String> tags, String title, String content, Map<String, String> extras) {
		pushActors.forEach(pushActor -> pushActor.pushToBroadcast(tenantId, tags, title, content, null, extras));
	}

	public void register(long tenantId, long userId, String clientId, String deviceModel) {
		for (PushActor pushActor : pushActors) {
			if (pushActor.supportDeviceModel(tenantId, deviceModel)) {
				PushUser pushUser = pushUserMapper.selectByPrimaryKey(userId);
				if (pushUser == null) {
					pushUser = new PushUser(userId, clientId, pushActor.getCode(), 0);
					pushUserMapper.insert(pushUser);
				} else if (!pushActor.getCode().equals(pushUser.getPushChannel()) ||
						!clientId.equals(pushUser.getClientId())) {
					pushUser = new PushUser(userId, clientId, pushActor.getCode(), 0);
					pushUserMapper.updateByPrimaryKey(pushUser);
				}
				return;
			}
		}
		throw new BusinessException("注册推送设备失败：无法找到合适的推送渠道！");
	}
	public void unregister(long tenantId, long userId) {
		pushUserMapper.deleteByPrimaryKey(userId);
	}
}
