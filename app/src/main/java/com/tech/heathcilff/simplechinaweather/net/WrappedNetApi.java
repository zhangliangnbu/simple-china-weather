package com.tech.heathcilff.simplechinaweather.net;

import com.tech.heathcilff.simplechinaweather.entity.BaseForecast;
import com.tech.heathcilff.simplechinaweather.entity.DailyForecast;
import com.tech.heathcilff.simplechinaweather.entity.NowForecast;
import com.tech.heathcilff.simplechinaweather.entity.StatusCode;
import com.tech.heathcilff.simplechinaweather.entity.WeatherResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * Created by zhangliang on 01/03/2017.
 */

public class WrappedNetApi {

	public static Observable<DailyForecast> dailyForecast(String city) {
		return commonHandle(NetService.api().dailyForecast(city));
	}

	public static Observable<NowForecast> nowForecast(String city) {
		return commonHandle(NetService.api().nowForecast(city));
	}

	private static <T extends BaseForecast> Observable<T> commonHandle(Observable<WeatherResult<T>> observable) {
		return observable
				.map(new Function<WeatherResult<T>, T>() {
					@Override
					public T apply(WeatherResult<T> tWeatherResult) throws Exception {
						T forecast = tWeatherResult.HeWeather5.get(0);
						if (!StatusCode.OK.status().equals(forecast.status)) {
							throw new NetException(forecast.status);
						}
						return forecast;
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
