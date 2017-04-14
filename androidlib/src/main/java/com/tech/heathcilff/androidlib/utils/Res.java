package com.tech.heathcilff.androidlib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by lulingzhi on 16/8/15.
 * res/资源
 */
public final class Res {
	private Res() {
	}

	public static String getString(@NonNull Context context, @StringRes int stringId, Object... formatArgs) {
		if (formatArgs.length > 0) {
			return context.getString(stringId, formatArgs);
		}
		return context.getString(stringId);
	}

	public static String getString(@StringRes int stringId, Object... formatArgs) {
		Context context = UtilConfig.getContext();
		if (formatArgs.length > 0) {
			return context.getString(stringId, formatArgs);
		}
		return context.getString(stringId);
	}

	@ColorInt
	public static int getColor(@NonNull Context context, @ColorRes int colorId) {
		return ContextCompat.getColor(context, colorId);
	}

	@ColorInt
	public static int getColor(@ColorRes int colorId) {
		return ContextCompat.getColor(UtilConfig.getContext(), colorId);
	}

	public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int colorId) {
		return ContextCompat.getColorStateList(context, colorId);
	}

	public static ColorStateList getColorStateList(@ColorRes int colorId) {
		return ContextCompat.getColorStateList(UtilConfig.getContext(), colorId);
	}

	public static Drawable getDrawable(@NonNull Context context, @DrawableRes int drawableId) {
		if (drawableId == 0) {
			return null;
		}
		return ContextCompat.getDrawable(context, drawableId);
	}

	public static Drawable getDrawable(@DrawableRes int drawableId) {
		if (drawableId == 0) {
			return null;
		}
		return ContextCompat.getDrawable(UtilConfig.getContext(), drawableId);
	}
}
