package com.tech.heathcilff.simplechinaweather.engine;

import android.app.AlertDialog;
import android.content.Context;

import com.tech.heathcilff.simplechinaweather.activity.MainActivity;
import com.tech.heathcilff.simplechinaweather.entity.HttpResult;
import com.tech.heathcilff.simplechinaweather.entity.Subject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangliang on 2017/2/28.
 */

public class HttpMethods {
	private static final String BASE_URL = "https://api.douban.com/v2/movie/";
	private static final int DEFAULT_TIMEOUT = 5000;
	private Retrofit retrofit;
	private MainActivity.MovieService movieService;

	public HttpMethods() {
		OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
				.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
		retrofit = new Retrofit.Builder()
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.client(okhttpClientBuilder.build())
				.baseUrl(BASE_URL)
				.build();
		movieService = retrofit.create(MainActivity.MovieService.class);
	}

	private static class SingletonHolder {
		private static final HttpMethods INSTANCE = new HttpMethods();
	}

	public static HttpMethods getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public Disposable getTopMovie(DisposableObserver<HttpResult<List<Subject>>> observer, int start, int count, final Context context) {
		return movieService
				.getTopMovie(start, count)
				.map(new Function<HttpResult<List<Subject>>, HttpResult<List<Subject>>>() {
					@Override
					public HttpResult<List<Subject>> apply(HttpResult<List<Subject>> listHttpResult) throws Exception {
						if(listHttpResult.resultCode != 0) throw new ApiException(listHttpResult.resultCode, listHttpResult.resultMessage);
						return listHttpResult;
					}
				})
				.subscribeOn(Schedulers.io())
				.unsubscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable disposable) throws Exception {
						AlertDialog dialog = new AlertDialog
								.Builder(context)
								.setMessage("show dialog")
								.create();
						dialog.show();
//						ProgressDialog progressDialog = new ProgressDialog(context);
//						progressDialog.show();
					}
				})
				.subscribeWith(observer);
	}

	public class ApiException extends Exception {
		public int code;
		public String message;

		public ApiException(int code, String message) {
			super(code + message);
			this.code = code;
			this.message = message;
		}
	}
}
