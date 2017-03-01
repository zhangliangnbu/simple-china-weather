package com.tech.heathcilff.simplechinaweather.entity;

/**
 *
 * Created by zhangliang on 2017/3/1.
 */

public class DayWeather {
	public Astro astro;//天文数值
	public DayCond cond;//天气状况
	public String date;//预报日期
	public String hum;//相对湿度（%）
	public String pcpn;//降水量（mm）
	public String pop;//降水概率
	public String pres; //气压
	public Tmp tmp;//温度
	public String uv;
	public String vis;//能见度（km）
	public Wind wind;//风力风向

	public class Astro {
		public String mr;
		public String ms;
		public String sr;
		public String ss;
	}

	public class DayCond {
		public String code_d;
		public String code_n;
		public String txt_d;
		public String txt_n;
	}

	public class Tmp {
		public String max;
		public String min;
	}

	@Override
	public String toString() {
		return "DayWeather{" +
				"astro=" + astro +
				", cond=" + cond +
				", date='" + date + '\'' +
				", hum='" + hum + '\'' +
				", pcpn='" + pcpn + '\'' +
				", pop='" + pop + '\'' +
				", pres='" + pres + '\'' +
				", tmp=" + tmp +
				", uv='" + uv + '\'' +
				", vis='" + vis + '\'' +
				", wind=" + wind +
				'}';
	}
}
