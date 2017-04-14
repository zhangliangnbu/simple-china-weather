package com.tech.heathcilff.simplechinaweather.engine;

import android.content.Intent;

import com.tech.heathcilff.simplechinaweather.helper.ApplicationHelper;
import com.tech.heathcilff.simplechinaweather.ui.CityManagementActivity;
import com.tech.heathcilff.simplechinaweather.ui.NavigationActivity;

/**
 * 页面跳转
 * Created by zhangliang on 10/04/2017.
 */

public class Intents {
	public static Intent toMainPage() {
		return new Intent(ApplicationHelper.getContext(), NavigationActivity.class);
	}

	public static Intent toCityManagement() {
		return new Intent(ApplicationHelper.getContext(), CityManagementActivity.class);
	}

	public static Intent toSettings() {
		return new Intent(ApplicationHelper.getContext(), CityManagementActivity.class);
	}

}
