package com.yoga.core.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLocker  implements DistributedLocker {

    private final static String LOCKER_PREFIX = "lock:";

    @Autowired
    private  RedissonConnector redissonConnector;

    @Override
    public <T> T lock(String resourceName, AquiredLockWorker<T> worker) throws InterruptedException, UnableToAquireLockException, Exception {
        return lock(resourceName, 100, 100, worker);
    }
    @Override
    public <T> T lock(String resourceName, int lockTime, AquiredLockWorker<T> worker) throws Exception {
        return lock(resourceName, lockTime, 100, worker);
    }
    @Override
    public <T> T lock(String resourceName, int lockTime, int waitTime, AquiredLockWorker<T> worker) throws UnableToAquireLockException, Exception {
        RedissonClient redisson= redissonConnector.getClient();
        RLock lock = redisson.getLock(LOCKER_PREFIX + resourceName);
        // Wait for waitTime seconds seconds and automatically unlock it after lockTime seconds
        boolean success = lock.tryLock(waitTime, lockTime, TimeUnit.SECONDS);
        if (success) {
            try {
                return worker.invokeAfterLockAquire();
            } finally {
                lock.unlock();
            }
        }
        throw new UnableToAquireLockException();
    }
}