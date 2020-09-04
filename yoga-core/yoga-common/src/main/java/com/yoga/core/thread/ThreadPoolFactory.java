package com.yoga.core.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池单例
 * 
 * @author Skysea
 *
 */
public class ThreadPoolFactory {
	private volatile static ThreadPoolExecutor EXECUTOR;

	private static int corePoolSize = 1;

	private static int maxPoolSize = 10;

	private static int keepAliveSeconds = 60;

	private static int queueCapacity = Integer.MAX_VALUE;

	private ThreadPoolFactory (){}

	public static ThreadPoolExecutor getInstance() {
		if (EXECUTOR == null) {
			synchronized (ThreadPoolFactory.class) {
				if (EXECUTOR == null) {
					BlockingQueue<Runnable> queue = createQueue(queueCapacity);
					EXECUTOR = new ThreadPoolExecutor(
							corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
							queue);
				}
			}
		}
		return EXECUTOR;
	}
	
	protected static BlockingQueue<Runnable> createQueue(int queueCapacity) {
		if (queueCapacity > 0) {
			return new LinkedBlockingQueue<Runnable>(queueCapacity);
		}
		else {
			return new SynchronousQueue<Runnable>();
		}
	}
}
