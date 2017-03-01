package com.tech.heathcilff.simplechinaweather.entity;

/**
 * Created by zhangliang on 2017/2/28.
 */

public class CityInfo {
	public String city;
	public String cnty;
	public String id;
	public String lat;
	public String lon;
	public Update update;

	public class Update {
		public String loc;
		public String utc;

		@Override
		public String toString() {
			return "Update{" +
					"loc='" + loc + '\'' +
					", utc='" + utc + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "CityInfo{" +
				"city='" + city + '\'' +
				", cnty='" + cnty + '\'' +
				", id='" + id + '\'' +
				", lat='" + lat + '\'' +
				", lon='" + lon + '\'' +
				", update=" + update +
				'}';
	}
}
