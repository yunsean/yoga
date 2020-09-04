package com.yoga.core.redis.lock;

public interface AquiredLockWorker<T> {
    T invokeAfterLockAquire() throws Exception;
}
