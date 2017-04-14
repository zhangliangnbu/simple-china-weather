package com.tech.heathcilff.simplechinaweather.net;

import com.tech.heathcilff.simplechinaweather.entity.BaiduCityInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by zhangliang on 2017/2/28.
 */

public interface MapNetApi {

	@GET("v2/")
	Observable<BaiduCityInfo> location(@Query("location") String location);
}
