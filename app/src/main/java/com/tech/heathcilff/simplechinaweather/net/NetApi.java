package com.tech.heathcilff.simplechinaweather.net;

import com.tech.heathcilff.simplechinaweather.entity.DailyForecast;
import com.tech.heathcilff.simplechinaweather.entity.NowForecast;
import com.tech.heathcilff.simplechinaweather.entity.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by zhangliang on 2017/2/28.
 */

public interface NetApi {

	@GET("forecast")
	Observable<WeatherResult<DailyForecast>> dailyForecast(@Query("city") String city);

	@GET("now")
	Observable<WeatherResult<NowForecast>> nowForecast(@Query("city") String city);



}
