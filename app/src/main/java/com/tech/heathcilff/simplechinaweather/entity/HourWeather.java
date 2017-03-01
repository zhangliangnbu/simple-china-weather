package com.tech.heathcilff.simplechinaweather.entity;

/**
 *
 * Created by zhangliang on 2017/3/1.
 */

public class HourWeather {
	private HourCond cond;
	private String fl;// 体感温度
	private String hum;
	private String pcpn;
	private String pres;
	private String tmp;
	private String vis;
	private Wind wind;

	public class HourCond {
		public String code;
		public String txt;
	}
}
