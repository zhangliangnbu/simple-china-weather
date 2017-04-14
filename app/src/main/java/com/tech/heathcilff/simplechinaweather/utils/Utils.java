package com.tech.heathcilff.simplechinaweather.utils;

import android.app.ProgressDialog;

import com.tech.heathcilff.androidlib.utils.BaseUtils;
import com.tech.heathcilff.simplechinaweather.ui.BaseActivity;

/**
 *
 * Created by zhangliang on 16/9/13.
 */
public class Utils extends BaseUtils {

	public static int convertToInt(Object value, int defaultValue) {
		if (value == null || "".equals(value.toString().trim())) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value.toString());
		} catch (Exception e) {
			try {
				return Double.valueOf(value.toString()).intValue();
			} catch (Exception e1) {
				return defaultValue;
			}
		}
	}

	public static ProgressDialog createProgressDialog(BaseActivity activity, String msg) {
		ProgressDialog dialog = new ProgressDialog(activity);
		dialog.setMessage(msg);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
}
