package com.yoga.core.service;

import com.yoga.core.behavior.service.BehaviorService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.redis.RedisOperator;
import com.yoga.core.redis.lock.RedisLocker;
import com.yoga.core.sequence.SequenceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {

	@Autowired
	protected RedisOperator redisOperator;
	@Autowired
	protected SequenceService sequenceService;
	@Autowired
	protected BehaviorService behaviorService;
	@Autowired
	protected RedisLocker redisLocker;
	
	protected Logger logger = Logger.getLogger(getClass());

	protected interface LockRunnable<T> {
		T run();
	}
	protected <T> T runInLock(String key, LockRunnable<T> runnable) {
		try {
			return redisLocker.lock(key, ()-> {
				return runnable.run();
			});
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
	protected void runInLock(String key, Runnable runnable) {
		try {
			redisLocker.lock(key, ()-> {
				runnable.run();
				return true;
			});
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
}
