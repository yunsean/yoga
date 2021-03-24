package com.yoga.core.redis.lock;

public interface DistributedLocker {
    <T> T lock(String resourceName, AquiredLockWorker<T> worker) throws UnableToAquireLockException, Exception;
    <T> T lock(String resourceName, int lockTime, AquiredLockWorker<T> worker) throws UnableToAquireLockException, Exception;
    <T> T lock(String resourceName, int lockTime, int waitTime, AquiredLockWorker<T> worker) throws UnableToAquireLockException, Exception;
}
