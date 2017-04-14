package com.tech.heathcilff.simplechinaweather.helper;

import android.app.Application;
import android.content.Context;

import com.tech.heathcilff.androidlib.utils.UtilConfig;
import com.tech.heathcilff.simplechinaweather.BuildConfig;

/**
 *
 * Created by zhangliang on 03/03/2017.
 */

public class ApplicationHelper {
	private static Application instance;

	private ApplicationHelper() {
	}

	public static void doOnCreate(Application application) {
		instance = application;
		UtilConfig.init(getContext(), BuildConfig.DEBUG);
	}

	public static Application instance() {
		return instance;
	}

	public static Context getContext() {
		return instance.getApplicationContext();
	}
}
