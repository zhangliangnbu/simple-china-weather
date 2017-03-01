package com.tech.heathcilff.simplechinaweather.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tech.heathcilff.androidlib.net.RequestCallback;
import com.tech.heathcilff.androidlib.net.RequestParameter;
import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.engine.HttpMethods;
import com.tech.heathcilff.simplechinaweather.engine.RemoteService;
import com.tech.heathcilff.simplechinaweather.entity.DailyForecast;
import com.tech.heathcilff.simplechinaweather.entity.HttpResult;
import com.tech.heathcilff.simplechinaweather.entity.StatusCode;
import com.tech.heathcilff.simplechinaweather.entity.Subject;
import com.tech.heathcilff.simplechinaweather.entity.WeatherResult;
import com.tech.heathcilff.simplechinaweather.net.NetException;
import com.tech.heathcilff.simplechinaweather.net.NetService;
import com.tech.heathcilff.simplechinaweather.utils.Utils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class MainActivity extends BaseActivity {

	private TextView textView;
	private Handler handler;
	private static final int OK = 1;
	private static final int ERROR = 0;
	private CompositeDisposable compositeDisposable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.data);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == OK) {
					textView.setText((CharSequence) msg.obj);
				} else {
					textView.setText("request failed");
				}
			}
		};
		compositeDisposable = new CompositeDisposable();
	}

	@Override
	protected void onPause() {
		super.onPause();
		compositeDisposable.clear();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		load();
//		loadData();
//		loadByRx();
//		rxJavaScheduler();
//		testRetrofit();
//		compositeDisposable.add(getMovie());
		compositeDisposable.add(getDailyForecast());
	}

	public Disposable getDailyForecast() {
		return NetService.api()
				.dailyForecast("beijing")
				.map(new Function<WeatherResult<DailyForecast>, DailyForecast>() {
					@Override
					public DailyForecast apply(WeatherResult<DailyForecast> dailyForecastWeatherResult) throws Exception {
						DailyForecast dailyForecast = dailyForecastWeatherResult.HeWeather5.get(0);
						if (!StatusCode.OK.status().equals(dailyForecast.status)) {
							throw new NetException(dailyForecast.status);
						}
						return dailyForecast;
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new DisposableObserver<DailyForecast>() {
					@Override
					public void onNext(DailyForecast value) {
						Log.d("onNext==", value.toString());
						textView.setText(value.toString());
					}

					@Override
					public void onError(Throwable e) {
						Log.e("onError==", e.getMessage());
					}

					@Override
					public void onComplete() {
						Log.d("onComplete==", "complete");
					}
				});
	}


//	----------------------------------------------------

	public Disposable getMovie() {
		return HttpMethods.getInstance().getTopMovie(new DisposableObserver<HttpResult<List<Subject>>>() {
			@Override
			public void onNext(HttpResult<List<Subject>> value) {
				Log.d("noNext===", value.toString());
				textView.setText(value.toString());
			}

			@Override
			public void onError(Throwable e) {
				Log.d("onError===", "onError");
				Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				Log.d("onComplete===", "onComplete");
			}
		}, 0, 10, this);

//		String baseUrl = "https://api.douban.com/v2/movie/";
//		Retrofit retrofit = new Retrofit.Builder()
//				.baseUrl(baseUrl)
//				.addConverterFactory(GsonConverterFactory.create())
//				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//				.build();
//		MovieService movieService = retrofit.create(MovieService.class);
//		movieService.getTopMovie(0, 10)
//				.subscribeOn(Schedulers.io())
//				.observeOn(AndroidSchedulers.mainThread())
//				.subscribe(new DisposableObserver<MovieEntity>() {
//					@Override
//					public void onNext(MovieEntity value) {
//						Log.d("noNext", value.toString());
//						textView.setText(value.toString());
//					}
//
//					@Override
//					public void onError(Throwable e) {
//						e.printStackTrace();
//					}
//
//					@Override
//					public void onComplete() {
//						Log.d("onComplete", "onComplete");
//					}
//				});
	}

	public interface MovieService {
		@GET("top250")
		Observable<HttpResult<List<Subject>>> getTopMovie(@Query("start") int start, @Query("count") int count);
	}

//	----------------------------------------------------

	public void testRetrofit() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		RetrofitTestApi retrofitTestApi = retrofit.create(RetrofitTestApi.class);
		Call<RetrofitTestModel> call = retrofitTestApi.repo("Guolei1130");
		call.enqueue(new Callback<RetrofitTestModel>() {
			@Override
			public void onResponse(Call<RetrofitTestModel> call, Response<RetrofitTestModel> response) {
				Log.d("retrofit", response.body().getLogin());
				textView.setText(response.body().getLogin());
			}

			@Override
			public void onFailure(Call<RetrofitTestModel> call, Throwable t) {
				Log.d("retrofit", t.getMessage());
			}
		});
	}

	public interface RetrofitTestApi {
		@GET("/users/{user}")
		Call<RetrofitTestModel> repo(@Path("user") String user);
	}

	public class RetrofitTestModel {
		private String login;

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}
	}
//	----------------------------------------------------

	public void rxJavaScheduler() {
		Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
			@Override
			public void subscribe(FlowableEmitter<String> e) throws Exception {
				e.onNext("display after 5s");
				SystemClock.sleep(5000);
				e.onNext("tiger world");
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER);

		flowable.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(String s) throws Exception {
						Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void loadByRx() {
		Observable.just("one", "two", "three", "four", "five")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<String>() {
					@Override
					public void onSubscribe(Disposable d) {
						Log.d("onSubscribe", "onSubscribe");
					}

					@Override
					public void onNext(String value) {
						Log.d("onNext", value);
					}

					@Override
					public void onError(Throwable e) {
						Log.d("onError", "onError");
					}

					@Override
					public void onComplete() {
						Log.d("onComplete", "onComplete");
					}
				});
		Subscriber subscriber = new Subscriber() {
			@Override
			public void onSubscribe(Subscription s) {

			}

			@Override
			public void onNext(Object o) {

			}

			@Override
			public void onError(Throwable t) {

			}

			@Override
			public void onComplete() {

			}
		};
	}

	private void load() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				qryData();
			}
		}).start();
	}

	private void qryData() {
		try {
			URL url = new URL("https://free-api.heweather.com/v5/weather?city=beijing&key=92cdcd3888f848ba8f4805e08633b175");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String temp;
				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}
				Message msg = Message.obtain();
				msg.obj = sb;
				msg.what = OK;
				handler.sendMessage(msg);
			} else {
				handler.sendEmptyMessage(ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadData() {
		// 回调
		RequestCallback requestCallback = new AbstractRequestCallback() {
			@Override
			public void onSuccess(String content) {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				Log.d("weather info", content + "--");
				textView.setText(content);
//				WeatherInfo weatherInfo = JSON.parseObject(content, WeatherInfo.class);
//				if(weatherInfo != null) {
//					tvCity.setText(weatherInfo.getCity().concat(weatherInfo.getCityid()));
//					tvCityId.setText(weatherInfo.getTemp().concat("摄氏度").concat(weatherInfo.getWD()).concat(weatherInfo.getWS()));
////					tvCity.setText(weatherInfo.city.concat(weatherInfo.cityid));
////					tvCityId.setText(weatherInfo.temp.concat("摄氏度").concat(weatherInfo.WD).concat(weatherInfo.WS));
////					Log.d("temp", weatherInfo.getTemp() + "");
////					Log.d("WD", weatherInfo.getWD() + "");
////					Log.d("WS", weatherInfo.getWS() + "");
//				}
			}
		};

		// 请求
		ArrayList<RequestParameter> parameters = new ArrayList<>();
		// 浦东 101021300; 上海 101020100; 杭州 101210101
		RequestParameter parameter1 = new RequestParameter("city", "beijing");
//		RequestParameter parameter2 = new RequestParameter("cityName", "Beijing");
		parameters.add(parameter1);
//		parameters.add(parameter2);

		// 执行
		progressDialog = Utils.createProgressDialog(this, getString(R.string.str_loading));
		progressDialog.show();
		RemoteService.getInstance().invoke(this, "getWeatherInfo", parameters, requestCallback, false);
	}
}
