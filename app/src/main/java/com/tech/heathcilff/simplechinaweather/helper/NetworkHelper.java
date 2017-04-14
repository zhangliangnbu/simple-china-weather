package com.tech.heathcilff.simplechinaweather.helper;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *
 * Created by zhangliang on 10/04/2017.
 */

public class NetworkHelper {
	public static boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) ApplicationHelper.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnectedOrConnecting();
	}
}
