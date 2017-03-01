package com.tech.heathcilff.simplechinaweather.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.tech.heathcilff.androidlib.net.RequestCallback;

/**
 *
 * Created by zhangliang on 2017/2/14.
 */

public abstract class BaseActivity extends AppCompatActivity {
	protected ProgressDialog progressDialog;

	/**
	 * 回调的统一处理
	 * onFail 默认统一处理. 如果需要特殊处理,复写即可.
	 */
	public abstract class AbstractRequestCallback implements RequestCallback {
		@Override
		public void onFail(String errorMessage) {
			if(progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			new AlertDialog.Builder(BaseActivity.this)
					.setTitle("出错啦")
					.setMessage(errorMessage)
					.setPositiveButton("确定", null)
					.show();
		}
	}
}
