package com.tech.heathcilff.simplechinaweather.net;

/**
 * Created by zhangliang on 2017/3/1.
 */

public class NetException extends Exception{
	private String status;

	public NetException(String status) {
		super(status);
		this.status = status;
	}
}
