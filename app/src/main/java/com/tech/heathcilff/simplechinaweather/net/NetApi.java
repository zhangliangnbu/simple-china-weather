package com.tech.heathcilff.simplechinaweather.net;

import com.tech.heathcilff.simplechinaweather.entity.AllWeatherForecast;
import com.tech.heathcilff.simplechinaweather.entity.BaseForecast;
import com.tech.heathcilff.simplechinaweather.entity.DailyForecast;
import com.tech.heathcilff.simplechinaweather.entity.NowForecast;
import com.tech.heathcilff.simplechinaweather.entity.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 *
 * Created by zhangliang on 2017/2/28.
 */

public interface NetApi {

	@Headers("Cache-Control: public, max-age=0")
	@GET("forecast")
	Observable<WeatherResult<DailyForecast>> dailyForecast(@Query("city") String city);

	@GET("now")
	Observable<WeatherResult<NowForecast>> nowForecast(@Query("city") String city);

	@GET("search")
	Observable<WeatherResult<BaseForecast>> searchCity(@Query("city") String city);

	@GET("weather")
	Observable<WeatherResult<AllWeatherForecast>> allWeatherForecast(@Query("city") String city);

}
