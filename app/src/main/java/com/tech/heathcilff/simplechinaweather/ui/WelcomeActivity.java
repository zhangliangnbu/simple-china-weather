package com.tech.heathcilff.simplechinaweather.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.tech.heathcilff.androidlib.utils.Pop;
import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.ui.binding.ActivityWelcomeBinding;
import com.tech.heathcilff.simplechinaweather.engine.Intents;
import com.tech.heathcilff.simplechinaweather.helper.NetworkHelper;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 展示最新信息、检查网络、检查更新等
 */
public class WelcomeActivity extends BaseActivity {

	private Animator animator;
	private boolean isConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityWelcomeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

		// animator
		animator = AnimatorInflater.loadAnimator(this, R.animator.welcome_animator);
		animator.setTarget(binding.imgWelcome);
		AnimatorListenerAdapter adapter = new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				initData();
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				checkAndHandle();
			}
		};
		animator.addListener(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		animator.start();
	}

	private void initData() {
		// check network
		Flowable.just(true)
				.observeOn(Schedulers.io())
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean aBoolean) throws Exception {
						isConnected = NetworkHelper.isConnected();
					}
				});

		// check version
	}

	private void checkAndHandle() {
		if(isConnected) {
			startActivity(Intents.toMainPage());
//			startActivity(new Intent(this, Main2Activity.class));
		} else {
			Pop.toast(getString(R.string.network_connectivity_error));
		}
		finish();
	}

}
