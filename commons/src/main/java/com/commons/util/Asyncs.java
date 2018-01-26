package com.commons.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * Date: 14-12-15
 *
 * @author jc.feng
 * @version V1.0
 */
public class Asyncs {

	private final static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(8, 512,
			1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1024), new DefaultThreadFactory("Asyncs"), new ThreadPoolExecutor.DiscardOldestPolicy());

	public static void execute(Runnable runnable) {
		THREAD_POOL.execute(runnable);
	}
	
	public static void submit(Runnable runnable) {
		THREAD_POOL.submit(runnable);
	}
	
	public static void shutdownNow() {
		THREAD_POOL.shutdownNow();
	}
}

