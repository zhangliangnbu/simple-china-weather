package com.tech.heathcilff.androidlib.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lulingzhi on 16/3/25.
 * 线程工具,并有内部线程池,
 */
public class ThreadUtil {
	private static volatile Handler mainThreadHandler;
	private static ExecutorService internalJob = Executors.newFixedThreadPool(3);
	private static ExecutorService serialJob = Executors.newSingleThreadExecutor();

	public static void runOnMainThread(Runnable r) {
		prepareHandler();
		mainThreadHandler.post(r);
	}

	public static void postOnMainThread(Runnable r, long delay) {
		prepareHandler();
		mainThreadHandler.postDelayed(r, delay);
	}
	
	public static void removeJob(Runnable r){
		prepareHandler();
		mainThreadHandler.removeCallbacks(r);
	}

	private static void prepareHandler() {
		if (mainThreadHandler == null) {
			synchronized (ThreadUtil.class) {
				if (mainThreadHandler == null) {
					mainThreadHandler = new Handler(Looper.getMainLooper());
				}
			}
		}
	}

	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	public static void submitWork(Runnable r) {
		internalJob.submit(r);
	}

	public static void submitSerial(Runnable r){
		serialJob.submit(r);
	}

	public static void assertOnMainThread(){
		if(!isOnMainThread()){
			throw new AssertionError("not on main thread!!!");
		}
	}
}
