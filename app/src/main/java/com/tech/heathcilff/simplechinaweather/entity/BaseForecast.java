package com.tech.heathcilff.simplechinaweather.entity;

/**
 * Created by zhangliang on 2017/3/1.
 */

public class BaseForecast {
	public CityInfo basic;
	public String status;

	@Override
	public String toString() {
		return "BaseForecast{" +
				"basic=" + basic +
				", status='" + status + '\'' +
				'}';
	}
}
