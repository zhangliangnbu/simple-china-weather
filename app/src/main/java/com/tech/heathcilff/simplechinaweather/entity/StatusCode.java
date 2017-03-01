package com.tech.heathcilff.simplechinaweather.entity;

/**
 * weather response status
 * Created by zhangliang on 2017/3/1.
 */


public enum  StatusCode {
	OK("ok"),
	INVALID_KEY("invalid key");

	private String status;

	StatusCode(String status) {
		this.status = status;
	}

	public String status() {
		return status;
	}
}
