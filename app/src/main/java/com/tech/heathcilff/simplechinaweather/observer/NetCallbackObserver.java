package com.tech.heathcilff.simplechinaweather.observer;

import io.reactivex.observers.DisposableObserver;

/**
 *
 * Created by zhangliang on 13/04/2017.
 */

public abstract class NetCallbackObserver<T> extends DisposableObserver<T> {

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onComplete() {

	}
}
