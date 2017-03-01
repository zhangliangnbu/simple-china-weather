package com.tech.heathcilff.simplechinaweather.entity;

/**
 * Created by zhangliang on 2017/2/28.
 */

public class HttpResult<T> {
	public int resultCode = 0;
	public String resultMessage = "ok";
	public int count;
	public int start;
	public int total;
	public T subjects;

	@Override
	public String toString() {
		return "HttpResult{" +
				"resultCode='" + resultCode + '\'' +
				", resultMessage='" + resultMessage + '\'' +
				", count=" + count +
				", start=" + start +
				", total=" + total +
				", subjects=" + subjects.toString() +
				'}';
	}
}
