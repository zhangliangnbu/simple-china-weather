package com.tech.heathcilff.simplechinaweather.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.binding.ActivityWelcomeBinding;
import com.tech.heathcilff.simplechinaweather.utils.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

public class WelcomeActivity extends BaseActivity {

	private ActivityWelcomeBinding binding;
	private Animator animator;
	private boolean isLogin = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// animator
		animator = AnimatorInflater.loadAnimator(this, R.animator.welcome_animator);
		animator.setTarget(binding.imgWelcome);

		// rx
		Flowable
				.create(new FlowableOnSubscribe<Object>() {
					@Override
					public void subscribe(final FlowableEmitter<Object> e) throws Exception {
						Logger.thread("FlowableOnSubscribe.subscribe");
						AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
							@Override
							public void onAnimationStart(Animator animation) {
								super.onAnimationStart(animation);
								e.onNext(new Object());
								Logger.thread("onAnimationStart");
							}

							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								e.onComplete();
								Logger.thread("onAnimationEnd");
							}
						};
						animator.addListener(adapter);
						animator.start();
					}
				}, BackpressureStrategy.BUFFER)
				.subscribeOn(AndroidSchedulers.mainThread())
//				.observeOn(Schedulers.io())
				.filter(new Predicate<Object>() {
					@Override
					public boolean test(Object aVoid) throws Exception {
						Logger.thread("filter.test");
						return true;// false then on next 不执行
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Subscriber<Object>() {
					@Override
					public void onSubscribe(Subscription s) {
						s.request(Long.MAX_VALUE);
						Logger.thread("Subscriber.onSubscribe");
					}

					@Override
					public void onNext(Object object) {
						Logger.thread("Subscriber.onNext");
						Toast.makeText(WelcomeActivity.this, "onNext", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(Throwable t) {
						Logger.thread("Subscriber.onError" + t.getMessage());
						t.printStackTrace();
					}

					@Override
					public void onComplete() {
						Logger.thread("Subscriber.onComplete");
						Toast.makeText(WelcomeActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
					}
				});
	}
//	D/Demo: [WelcomeActivity:onSubscribe:83]: Subscriber.onSubscribe == main thread:true
//	D/Demo: [WelcomeActivity:subscribe:49]: FlowableOnSubscribe.subscribe == main thread:true
//	D/Demo: [WelcomeActivity:onAnimationStart:55]: onAnimationStart == main thread:true
//	D/Demo: [WelcomeActivity:test:0]: filter.test == main thread:false
//	D/Demo: [WelcomeActivity:onNext:88]: Subscriber.onNext == main thread:true
//	D/Demo: [WelcomeActivity:onAnimationEnd:62]: onAnimationEnd == main thread:true
//	D/Demo: [WelcomeActivity:onComplete:100]: Subscriber.onComplete == main thread:true
}
