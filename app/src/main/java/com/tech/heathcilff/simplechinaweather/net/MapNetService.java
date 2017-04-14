package com.tech.heathcilff.simplechinaweather.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * baidu map net service
 * Created by zhangliang on 2017/2/28.
 */

public class MapNetService {
	private static MapNetApi netApi = createNetApi();
	private static final String API_BASE_URL = "https://api.map.baidu.com/geocoder/";
	private static final String API_KEY_NAME = "ak";
	private static final String API_KEY_VALUE = "f9G6ms4LED0IgYyoqZqUAoGfdW8fnGI3";

	private MapNetService() {
	}

	public static MapNetApi api() {
		return netApi;
	}

	private static MapNetApi createNetApi() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(getParameterInterceptor())
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(API_BASE_URL)
				.client(okHttpClient)
				.build();
		return retrofit.create(MapNetApi.class);
	}

	private static Interceptor getParameterInterceptor() {
		return new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				HttpUrl.Builder authorizedUrlBuilder = request.url()
						.newBuilder()
						.scheme(request.url().scheme())
						.host(request.url().host())
						.addQueryParameter(API_KEY_NAME, API_KEY_VALUE)
						.addQueryParameter("output", "json");
				Request newRequest = request.newBuilder()
						.method(request.method(), request.body())
						.url(authorizedUrlBuilder.build())
						.removeHeader("Pragma")
						.build();
				Log.d("url==", newRequest.url().toString());
				return chain.proceed(newRequest);// network callback -350ms 此步中进行缓存处理
			}
		};
	}

}
