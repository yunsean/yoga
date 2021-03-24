package com.yoga.core.base;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.redis.RedisOperator;
import com.yoga.core.redis.lock.RedisLocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class BaseService {
	protected Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	protected RedisLocker redisLocker;
	protected interface LockRunnable<T> {
		T run();
	}

	@Autowired
	protected RedisOperator redisOperator;

	//返回秒为单位的时间戳
	protected Integer dateToTimeStamp(Date date) {
		if (date == null) return null;
		return (int)(date.getTime() / 1000);
	}

	protected <T> T runInLock(String key, LockRunnable<T> runnable) {
		return runInLock(key, 100, 100, runnable);
	}
	protected <T> T runInLock(String key, int lockTime, int waitTime, LockRunnable<T> runnable) {
		try {
			return redisLocker.lock(key, lockTime, waitTime, runnable::run);
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
	protected void runInLock(String key, Runnable runnable) {
		runInLock(key, 100, 100, runnable);
	}
	protected void runInLock(String key, int lockTime, int waitTime, Runnable runnable) {
		try {
			redisLocker.lock(key, lockTime, waitTime, ()-> {
				runnable.run();
				return true;
			});
		} catch (BusinessException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new BusinessException(ex.getMessage());
		}
	}
}
