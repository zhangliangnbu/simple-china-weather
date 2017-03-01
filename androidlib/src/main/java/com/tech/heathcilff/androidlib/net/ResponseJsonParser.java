package com.tech.heathcilff.androidlib.net;

/**
 * 从返回的json中获取需要的部分
 * Created by zhangliang on 2017/2/15.
 */

public interface ResponseJsonParser {
	String parse(String result);
}
