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
 * net service
 * Created by zhangliang on 2017/2/28.
 */

public class NetService {
	private static NetApi netApi = createNetApi();
	private static final String API_BASE_URL = "https://free-api.heweather.com/v5/";
	private static final String API_KEY_NAME = "key";
	private static final String API_KEY_VALUE = "92cdcd3888f848ba8f4805e08633b175";

	private NetService(){}
	private static NetApi createNetApi() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				HttpUrl.Builder authorizedUrlBuilder = request.url()
						.newBuilder()
						.scheme(request.url().scheme())
						.host(request.url().host())
						.addQueryParameter(API_KEY_NAME, API_KEY_VALUE);
				Request newRequest = request.newBuilder()
						.method(request.method(), request.body())
						.url(authorizedUrlBuilder.build())
						.build();
				Log.d("url", newRequest.url().toString());
				return chain.proceed(newRequest);
			}
		}).build();

		Retrofit retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(API_BASE_URL)
				.client(okHttpClient)
				.build();
		return retrofit.create(NetApi.class);
	}

	public static NetApi api() {
		return netApi;
	}
}
