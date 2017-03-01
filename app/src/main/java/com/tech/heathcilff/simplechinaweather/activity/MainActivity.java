package com.tech.heathcilff.simplechinaweather.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.entity.DailyForecast;
import com.tech.heathcilff.simplechinaweather.net.WrappedNetApi;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends BaseActivity {

	private TextView textView;
	private CompositeDisposable compositeDisposable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.data);
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
		compositeDisposable.add(getDailyForecast());
	}

	public Disposable getDailyForecast() {
		return WrappedNetApi.dailyForecast("beijing")
				.doOnSubscribe(new Consumer<Disposable>() {
					@Override
					public void accept(Disposable disposable) throws Exception {
						showLoadingAnimation();
					}
				})
				.subscribeWith(new DisposableObserver<DailyForecast>() {
					@Override
					public void onNext(DailyForecast value) {
						Log.d("onNext==", value.toString());
						hideLoadingAnimation();
						textView.setText(value.toString());
					}

					@Override
					public void onError(Throwable e) {
						Log.e("onError==", e.getMessage());
						hideLoadingAnimation();
					}

					@Override
					public void onComplete() {
						Log.d("onComplete==", "complete====");
						hideLoadingAnimation();
					}
				});
	}
}
