package com.tech.heathcilff.simplechinaweather.entity;

import java.util.List;

/**
 * Created by zhangliang on 2017/2/28.
 */

public class MovieEntity {
	public String resultCode = "0";
	public String resultMessage = "ok";
	public int count;
	public int start;
	public int total;
	public List<Subject> subjects;

	@Override
	public String toString() {
		return "MovieEntity{" +
				"count=" + count +
				", start=" + start +
				", total=" + total +
				", subjects=" + subjects.toString() +
				'}';
	}
}
