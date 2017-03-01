package com.tech.heathcilff.simplechinaweather.entity;

import java.util.List;

/**
 *
 * Created by zhangliang on 2017/3/1.
 */

public class DailyForecast extends BaseForecast {
	public List<DayWeather> daily_forecast;

	@Override
	public String toString() {
		return super.toString() + daily_forecast.toString();
	}
}
