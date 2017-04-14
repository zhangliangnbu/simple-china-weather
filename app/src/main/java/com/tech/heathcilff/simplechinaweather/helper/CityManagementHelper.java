package com.tech.heathcilff.simplechinaweather.helper;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tech.heathcilff.androidlib.utils.SharePreferencesUtil;
import com.tech.heathcilff.simplechinaweather.entity.CityInfo;

import java.util.ArrayList;

/**
 *
 * Created by zhangliang on 12/04/2017.
 */

public class CityManagementHelper {
	private static final String SHARE_PREF_CITY = "share_pref_city";
	private static final String KEY_CURRENT_CITY = "key_current_city";
	private static final String KEY_CITY_LIST = "key_city_list";
	private static final String CITY_SEPARATOR = "###";

	public static void updateCurrentCity(CityInfo cityInfo) {
		SharedPreferences sharedPref = SharePreferencesUtil.getSharePref(SHARE_PREF_CITY);
		String json = new Gson().toJson(cityInfo);
		sharedPref.edit().putString(KEY_CURRENT_CITY, json).apply();
	}

	public static CityInfo getCurrentCity() {
		SharedPreferences sharedPref = SharePreferencesUtil.getSharePref(SHARE_PREF_CITY);
		String json = sharedPref.getString(KEY_CURRENT_CITY, "");
		if(TextUtils.isEmpty(json)) {
			return new CityInfo();
		} else {
			return new Gson().fromJson(json, CityInfo.class);
		}
	}

	public static void addCity(CityInfo cityInfo) {
		SharedPreferences sharedPref = SharePreferencesUtil.getSharePref(SHARE_PREF_CITY);
		String cities = sharedPref.getString(KEY_CITY_LIST, "");
		Gson gson = new Gson();
		if(TextUtils.isEmpty(cities)) {
			cities = gson.toJson(cityInfo);
		} else {
			cities = cities + CITY_SEPARATOR + gson.toJson(cityInfo);
		}
		sharedPref.edit().putString(KEY_CITY_LIST, cities).apply();
	}

	public static ArrayList<CityInfo> getCitys() {
		SharedPreferences sharedPref = SharePreferencesUtil.getSharePref(SHARE_PREF_CITY);
		String cities = sharedPref.getString(KEY_CITY_LIST, "");
		ArrayList<CityInfo> list = new ArrayList<>();
		if(!TextUtils.isEmpty(cities)) {
			String[] citiesStr = cities.split(CITY_SEPARATOR);
			Gson gson = new Gson();
			for(String s : citiesStr) {
				list.add(gson.fromJson(s, CityInfo.class));
			}
		}
		return list;
	}

	public static boolean isEqual(CityInfo c1, CityInfo c2) {
		return c1 != null && c2 != null && TextUtils.equals(c1.id, c2.id);
	}
}
