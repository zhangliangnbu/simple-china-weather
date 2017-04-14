package com.tech.heathcilff.simplechinaweather.entity;

/**
 *
 * Created by zhangliang on 2017/3/1.
 */

public class HourWeather {
	public HourCond cond;
	public String fl;// 体感温度
	public String hum;
	public String pcpn;
	public String pres;
	public String tmp;
	public String vis;
	public Wind wind;

	public class HourCond {
		public String code;
		public String txt;
	}
}
