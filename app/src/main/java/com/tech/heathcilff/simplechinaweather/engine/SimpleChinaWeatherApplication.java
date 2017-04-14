package com.tech.heathcilff.simplechinaweather.engine;

import android.app.Application;

import com.tech.heathcilff.androidlib.cache.CacheManager;
import com.tech.heathcilff.simplechinaweather.helper.ApplicationHelper;

import java.io.File;


/**
 *
 * Created by zhangliang on 16/9/18.
 */

public class SimpleChinaWeatherApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ApplicationHelper.doOnCreate(this);

		// 初始化缓存路径
		CacheManager.getInstance().initCacheDirectory(getCacheDir().getPath() + File.separator +  "response");
	}
}
