package com.tech.heathcilff.androidlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by zhangliang on 12/04/2017.
 */

public class SharePreferencesUtil {

	public static SharedPreferences getSharePref(String spName) {
		return UtilConfig.getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
	}

}
