package com.tech.heathcilff.simplechinaweather.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.tech.heathcilff.androidlib.utils.Res;
import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.entity.HourWeather;

/**
 *
 * Created by zhangliang on 13/04/2017.
 */

public class BaseWeatherModel extends BaseViewModel {
	public ObservableField<CharSequence> condDescription = new ObservableField<>();
	public ObservableField<CharSequence> temperature = new ObservableField<>();
	public ObservableField<CharSequence> somatosensoryTemperature = new ObservableField<>();
	public ObservableField<CharSequence> relativeHumidity = new ObservableField<>();

	public void set(HourWeather hourWeather) {
		condDescription.set(hourWeather.cond.txt);
		temperature.set(TextUtils.concat(hourWeather.tmp, "°"));
		somatosensoryTemperature.set(TextUtils.concat(Res.getString(R.string.somatosensory_temperature), hourWeather.fl, "°"));
		relativeHumidity.set(TextUtils.concat(Res.getString(R.string.humidity), hourWeather.hum));
	}
}
