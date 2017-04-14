package com.tech.heathcilff.simplechinaweather.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.tech.heathcilff.androidlib.cache.CacheManager;
import com.tech.heathcilff.androidlib.net.PersistentCookieStore;
import com.tech.heathcilff.simplechinaweather.helper.ApplicationHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
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

	private NetService() {
	}

	public static NetApi api() {
		return netApi;
	}

	private static NetApi createNetApi() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(getParameterInterceptor())
				.addNetworkInterceptor(getCacheInterceptor())
//				.addInterceptor(getMockInterceptor())
				.cache(getCache())
				.cookieJar(getCookieJar())
				.build();

		Retrofit retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(API_BASE_URL)
				.client(okHttpClient)
				.build();
		return retrofit.create(NetApi.class);
	}

	private static Interceptor getMockInterceptor() {
		final String json = "{\n" +
				"\"HeWeather5\": [\n" +
				"{\n" +
				"\"basic\": {\n" +
				"\"city\": \"xijing京\",\n" +
				"\"cnty\": \"中国\",\n" +
				"\"id\": \"CN101010100\",\n" +
				"\"lat\": \"39.904000\",\n" +
				"\"lon\": \"116.391000\",\n" +
				"\"update\": {\n" +
				"\"loc\": \"2017-03-07 14:51\",\n" +
				"\"utc\": \"2017-03-07 06:51\"\n" +
				"}\n" +
				"},\n" +
				"\"daily_forecast\": [\n" +
				"{\n" +
				"\"astro\": {\n" +
				"\"mr\": \"12:48\",\n" +
				"\"ms\": \"02:35\",\n" +
				"\"sr\": \"06:38\",\n" +
				"\"ss\": \"18:12\"\n" +
				"},\n" +
				"\"cond\": {\n" +
				"\"code_d\": \"100\",\n" +
				"\"code_n\": \"100\",\n" +
				"\"txt_d\": \"晴\",\n" +
				"\"txt_n\": \"晴\"\n" +
				"},\n" +
				"\"date\": \"2017-03-07\",\n" +
				"\"hum\": \"22\",\n" +
				"\"pcpn\": \"0.0\",\n" +
				"\"pop\": \"0\",\n" +
				"\"pres\": \"1025\",\n" +
				"\"tmp\": {\n" +
				"\"max\": \"9\",\n" +
				"\"min\": \"0\"\n" +
				"},\n" +
				"\"uv\": \"3\",\n" +
				"\"vis\": \"10\",\n" +
				"\"wind\": {\n" +
				"\"deg\": \"326\",\n" +
				"\"dir\": \"北风\",\n" +
				"\"sc\": \"3-4\",\n" +
				"\"spd\": \"16\"\n" +
				"}\n" +
				"},\n" +
				"{\n" +
				"\"astro\": {\n" +
				"\"mr\": \"13:48\",\n" +
				"\"ms\": \"03:31\",\n" +
				"\"sr\": \"06:36\",\n" +
				"\"ss\": \"18:13\"\n" +
				"},\n" +
				"\"cond\": {\n" +
				"\"code_d\": \"100\",\n" +
				"\"code_n\": \"100\",\n" +
				"\"txt_d\": \"晴\",\n" +
				"\"txt_n\": \"晴\"\n" +
				"},\n" +
				"\"date\": \"2017-03-08\",\n" +
				"\"hum\": \"24\",\n" +
				"\"pcpn\": \"0.0\",\n" +
				"\"pop\": \"0\",\n" +
				"\"pres\": \"1021\",\n" +
				"\"tmp\": {\n" +
				"\"max\": \"13\",\n" +
				"\"min\": \"2\"\n" +
				"},\n" +
				"\"uv\": \"3\",\n" +
				"\"vis\": \"10\",\n" +
				"\"wind\": {\n" +
				"\"deg\": \"313\",\n" +
				"\"dir\": \"北风\",\n" +
				"\"sc\": \"3-4\",\n" +
				"\"spd\": \"10\"\n" +
				"}\n" +
				"},\n" +
				"{\n" +
				"\"astro\": {\n" +
				"\"mr\": \"14:50\",\n" +
				"\"ms\": \"04:21\",\n" +
				"\"sr\": \"06:35\",\n" +
				"\"ss\": \"18:14\"\n" +
				"},\n" +
				"\"cond\": {\n" +
				"\"code_d\": \"100\",\n" +
				"\"code_n\": \"100\",\n" +
				"\"txt_d\": \"晴\",\n" +
				"\"txt_n\": \"晴\"\n" +
				"},\n" +
				"\"date\": \"2017-03-09\",\n" +
				"\"hum\": \"19\",\n" +
				"\"pcpn\": \"0.0\",\n" +
				"\"pop\": \"0\",\n" +
				"\"pres\": \"1018\",\n" +
				"\"tmp\": {\n" +
				"\"max\": \"15\",\n" +
				"\"min\": \"2\"\n" +
				"},\n" +
				"\"uv\": \"4\",\n" +
				"\"vis\": \"10\",\n" +
				"\"wind\": {\n" +
				"\"deg\": \"302\",\n" +
				"\"dir\": \"南风\",\n" +
				"\"sc\": \"微风\",\n" +
				"\"spd\": \"9\"\n" +
				"}\n" +
				"}\n" +
				"],\n" +
				"\"status\": \"ok\"\n" +
				"}\n" +
				"]\n" +
				"}";

		return new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {

				return new Response.Builder()
						.code(200)
						.request(chain.request())
						.protocol(Protocol.HTTP_1_0)
						.body(ResponseBody.create(MediaType.parse("application/json"), json.getBytes()))
						.addHeader("content-type", "application/json")
						.message(json)
						.build();
			}
		};
	}

	@NonNull
	private static Cache getCache() {
		return new Cache(CacheManager.getInstance().getCacheDirectory(), 1024 * 1024 * 10);
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
						.addQueryParameter(API_KEY_NAME, API_KEY_VALUE);
				Request newRequest = request.newBuilder()
						.method(request.method(), request.body())
						.url(authorizedUrlBuilder.build())
						.removeHeader("Pragma")
						.build();
				return chain.proceed(newRequest);// network callback -350ms 此步中进行缓存处理
			}
		};
	}

	private static Interceptor getCacheInterceptor() {
		return new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				Response response = chain.proceed(request);

				String cacheControl = request.cacheControl().toString();
				if (TextUtils.isEmpty(cacheControl)) {
					cacheControl = "public, max-age=0";
				}
				return response.newBuilder()
						.header("Cache-Control", cacheControl)
						.removeHeader("Pragma")
						.build();
			}
		};
	}



	private static CookieJar getCookieJar() {
		// 持久化
		CookieJar inDiskCookieJar = new CookieJar() {
			final PersistentCookieStore cookieStore = new PersistentCookieStore(ApplicationHelper.instance());

			// 如果response header中没有"Set-Cookie"字段，则此方法不会执行
			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				if (cookies != null && cookies.size() > 0) {
					for (Cookie item : cookies) {
						cookieStore.add(url, item);
					}
				}
			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {
				List<Cookie> cookies = cookieStore.get(url);
				return cookies == null ? new ArrayList<Cookie>() : cookies;
			}
		};

		// 非持久化
		CookieJar inMemoryCookieJar = new CookieJar() {
			final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				Log.e(url.url().toString(), cookies.toString());
				cookieStore.put(url, cookies);
			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {
				List<Cookie> cookies = cookieStore.get(url);
				return cookies == null ? new ArrayList<Cookie>() : cookies;
			}
		};

		return inDiskCookieJar;
	}
}
