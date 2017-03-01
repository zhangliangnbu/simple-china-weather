package com.tech.heathcilff.simplechinaweather.engine;

import android.app.Application;

import com.tech.heathcilff.androidlib.cache.CacheManager;


/**
 *
 * Created by zhangliang on 16/9/18.
 */

public class SimpleChinaWeatherApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化缓存路径
		CacheManager.getInstance().initCacheDir();
	}
}
