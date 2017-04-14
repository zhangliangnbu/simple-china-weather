package com.tech.heathcilff.simplechinaweather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.tech.heathcilff.simplechinaweather.helper.ApplicationHelper;
import com.tech.heathcilff.simplechinaweather.utils.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("com.tech.heathcilff.simplechinaweather", appContext.getPackageName());
	}

	@Test
	public void testCacheDir() throws Exception {
		File file = ApplicationHelper.instance().getCacheDir();
		Log.d("getPath==", file.getPath());
		Log.d("getAbsolutePath==", file.getAbsolutePath());
		Log.d("getCanonicalPath==", file.getCanonicalPath());
	}

	@Test
	public void testTextUtils() {
		List<Integer> list = Arrays.asList(1, 5, 9);
		print(TextUtils.join("--", list));
	}

	private void print(String s) {
		System.out.println(s);
	}

	@Test
	public void getLocation() {
		Context appContext = InstrumentationRegistry.getTargetContext();
		LocationManager lm = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Logger.d("gps is normal");
		}
		Logger.d(lm.getAllProviders().toString());

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		// 使用省电模式
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 获得当前的位置提供者
		String provider = lm.getBestProvider(criteria, true);
		Logger.d("==" + provider);
		// 获得当前的位置
		if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		Location location = lm.getLastKnownLocation(provider);

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		Logger.d("==" + latitude + "," + longitude);
	}
}
