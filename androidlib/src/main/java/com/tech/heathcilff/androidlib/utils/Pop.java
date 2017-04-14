package com.tech.heathcilff.androidlib.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lulingzhi on 16/5/11.
 * 用于弹出各种浮出界面
 *
 * 弹出的toast可以取消
 * todo 其它浮层
 */
public class Pop {

	public static final long DURATION_LONG = 3500;
	public static final long DURATION_SHORT = 2000;

	/**
	 * Toast show LENGTH_SHORT
	 * @param msg toast msg
	 */
	public static void toast(final CharSequence msg){
		toast(msg, Toast.LENGTH_SHORT);
	}
	
	public static void toast(@StringRes final int msgRes){
		toast(msgRes, Toast.LENGTH_SHORT);
	}

	/**
	 * 设置Toast的位置为center
	 * @param msg
	 */
	public static void toastCenter(final CharSequence msg) {
		toastBuilder(msg).gravity(Gravity.CENTER).show();
	}

	/**
	 * Toast show LENGTH_LONG
	 * @param msg toast msg
	 */
	public static void longToast(final CharSequence msg){
		toast(msg, Toast.LENGTH_LONG);
	}
	public static void longToast(@StringRes final int msgRes){
		toast(msgRes, Toast.LENGTH_LONG);
	}
	
	private static void toast(final CharSequence msg, final int duration) {
		@SuppressLint("ShowToast")
		final Toast toast = Toast.makeText(getContext(), msg, duration);
		showToastOnMainThread(toast);
	}
	private static void toast(@StringRes final int msgRes, final int duration) {
		@SuppressLint("ShowToast")
		final Toast toast = Toast.makeText(getContext(), msgRes, duration);
		showToastOnMainThread(toast);
	}

	private static void showToastOnMainThread(final Toast toast) {
		if (ThreadUtil.isOnMainThread()) {
			show(toast);
		} else {
			ThreadUtil.runOnMainThread(new Runnable() {
				@Override
				public void run() {
					show(toast);
				}
			});
		}
	}


	private static Toast prevToast;
	@MainThread
	public static void show(final Toast toast){
		if(!ThreadUtil.isOnMainThread()){
			Log.w("Pop","call toast show outside of main thread");
			return;
		}
		cancelToast();
		toast.show();
		prevToast = toast;
		ThreadUtil.postOnMainThread(new Runnable() {
			@Override
			public void run() {
				if(prevToast == toast){
					prevToast = null;
				}
			}
		}, getDurationInMillis(toast));
	}

	private static long getDurationInMillis(Toast toast) {
		return toast.getDuration() == Toast.LENGTH_LONG ? DURATION_LONG : DURATION_SHORT;
	}

	/**
	 * Cancel previous toast shown by
	 * {@link #toast(CharSequence)} or {@link #longToast(CharSequence)}.
	 * if previous toast has been dismissed, nothing will happen
	 */
	public static void cancelToast() {
		if(prevToast != null && prevToast.getView().getWindowToken() != null){
			prevToast.cancel();
			Log.d("toast","canceled a toast");
		}
	}

	private static Context getContext(){
		return UtilConfig.getContext();
	}

	public static ToastBuilder toastBuilder(CharSequence text,@ToastDuration int duration){
		return new ToastBuilder(text,duration);
	}

	public static ToastBuilder toastBuilder(CharSequence text){
		return new ToastBuilder(text, ToastDuration.LENGTH_SHORT);
	}



	@SuppressLint("ShowToast")
	public static class ToastBuilder{

		private final Toast toast;

		private ToastBuilder(CharSequence text,int duration){
			this.toast = Toast.makeText(getContext(),text,duration);
		}

		public ToastBuilder duration(@ToastDuration int duration){
			this.toast.setDuration(duration);
			return this;
		}

		public ToastBuilder gravity(int gravity, int xOffset, int yOffset){
			this.toast.setGravity(gravity,xOffset,yOffset);
			return this;
		}

		public ToastBuilder gravity(int gravity){
			this.toast.setGravity(gravity,0,0);
			return this;
		}

		public ToastBuilder margin(float horizontalMargin, float verticalMargin){
			this.toast.setMargin(horizontalMargin,verticalMargin);
			return this;
		}

		/**
		 * 自定义Toast的View,必须有id为{@link android.R.id#message}的{@link TextView}
		 * @param customView 自定义view
		 */
		public ToastBuilder customView(@NonNull View customView){
			View tvNew = customView.findViewById(android.R.id.message);
			if(tvNew==null || !(tvNew instanceof TextView)){
				throw new RuntimeException("This custom view of Toast should have a proper " +
				                           "TextView with id = android.R.id.message");
			}

			View tv = this.toast.getView().findViewById(android.R.id.message);
			this.toast.setView(customView);
			if(tv!=null && tv instanceof TextView){
				text(((TextView) tv).getText());
			}
			return this;
		}


		public ToastBuilder text(@StringRes int textRes){
			this.toast.setText(textRes);
			return this;
		}

		public ToastBuilder text(CharSequence text){
			this.toast.setText(text);
			return this;
		}

		public Toast show(){
			Pop.show(this.toast);
			return this.toast;
		}
	}

	@IntDef({ToastDuration.LENGTH_SHORT, ToastDuration.LENGTH_LONG})
	@Retention(RetentionPolicy.SOURCE)
	public @interface ToastDuration {

		/**
		 * Show the view or text notification for a short period of time.  This time
		 * could be user-definable.  This is the default.
		 */
		int LENGTH_SHORT = Toast.LENGTH_SHORT;

		/**
		 * Show the view or text notification for a long period of time.  This time
		 * could be user-definable.
		 */
		int LENGTH_LONG = Toast.LENGTH_LONG;
	}
}
