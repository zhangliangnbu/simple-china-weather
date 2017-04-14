package com.tech.heathcilff.simplechinaweather.entity;

import java.util.List;

/**
 *
 * Created by zhangliang on 13/04/2017.
 */

public class AllWeatherForecast extends BaseForecast {
	public WrappedAirQuality aqi;
	public List<DayWeather> daily_forecast ;
	public List<HourWeather> hourly_forecast ;
	public HourWeather now;
	public Suggestion suggestion;

	public class WrappedAirQuality {
		public AirQuality city;
	}

	public class AirQuality {
		public String aqi;
		public String co;
		public String no2;
		public String o3;
		public String pm10;
		public String pm25;
		public String qlty;
		public String so2;
	}
}
