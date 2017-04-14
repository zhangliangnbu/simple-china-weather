package com.tech.heathcilff.simplechinaweather.viewmodel;

import com.tech.heathcilff.simplechinaweather.entity.AllWeatherForecast;

/**
 *
 * Created by zhangliang on 13/04/2017.
 */

public class AllWeatherModel extends BaseViewModel {
	public BaseWeatherModel baseWeatherModel = new BaseWeatherModel();
	public void set(AllWeatherForecast forecast) {
		baseWeatherModel.set(forecast.now);
	}
}
