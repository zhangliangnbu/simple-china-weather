package com.tech.heathcilff.simplechinaweather.utils;

import android.os.Looper;
import android.text.TextUtils;

import static java.lang.Thread.currentThread;


public final class Logger {

	private static final String TAG = "Demo";

	/**
	 * Set true or false if you want read logs or not
	 */
	private static boolean logEnabled_d = true;
	private static boolean logEnabled_i = true;
	private static boolean logEnabled_e = true;

	public static void d() {
		if (logEnabled_d) {
			android.util.Log.d(TAG, getLocation());
		}
	}

	public static void d(String msg) {
		if (logEnabled_d) {
			android.util.Log.d(TAG, getLocation() + msg);
		}
	}

	public static void i(String msg) {
		if (logEnabled_i) {
			android.util.Log.i(TAG, getLocation() + msg);
		}
	}

	public static void i() {
		if (logEnabled_i) {
			android.util.Log.i(TAG, getLocation());
		}
	}

	public static void e(String msg) {
		if (logEnabled_e) {
			android.util.Log.e(TAG, getLocation() + msg);
		}
	}

	public static void e(String msg, Throwable e) {
		if (logEnabled_e) {
			android.util.Log.e(TAG, getLocation() + msg, e);
		}
	}

	public static void e(Throwable e) {
		if (logEnabled_e) {
			android.util.Log.e(TAG, getLocation(), e);
		}
	}

	public static void e() {
		if (logEnabled_e) {
			android.util.Log.e(TAG, getLocation());
		}
	}

	private static String getLocation() {
		final String className = Logger.class.getName();
		final StackTraceElement[] traces = currentThread()
				.getStackTrace();

		boolean found = false;

		for (StackTraceElement trace : traces) {
			try {
				if (found) {
					if (!trace.getClassName().startsWith(className)) {
						Class<?> clazz = Class.forName(trace.getClassName());
						return "[" + getClassName(clazz) + ":"
								+ trace.getMethodName() + ":"
								+ trace.getLineNumber() + "]: ";
					}
				} else if (trace.getClassName().startsWith(className)) {
					found = true;
				}
			} catch (ClassNotFoundException ignored) {
			}
		}

		return "[]: ";
	}

	private static String getClassName(Class<?> clazz) {
		if (clazz != null) {
			if (!TextUtils.isEmpty(clazz.getSimpleName())) {
				return clazz.getSimpleName();
			}

			return getClassName(clazz.getEnclosingClass());
		}

		return "";
	}

	public static void thread(String tag) {
		boolean isMain = Looper.myLooper() == Looper.getMainLooper();
		Logger.d(tag + " == main thread:" + isMain);

//		Flowable.just(Thread.currentThread() == Looper.getMainLooper().getThread())
//				.filter(new Predicate<Boolean>() {
//					@Override
//					public boolean test(Boolean aBoolean) throws Exception {
//						if (!aBoolean) {
//							Logger.d("not main thread");
//						}
//						return false;
//					}
//				})
//				.subscribe(new Consumer<Boolean>() {
//					@Override
//					public void accept(Boolean aBoolean) throws Exception {
//						Logger.d("in main thread");
//					}
//				});
	}
}
