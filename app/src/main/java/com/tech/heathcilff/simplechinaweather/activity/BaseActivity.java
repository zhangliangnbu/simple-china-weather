package com.tech.heathcilff.simplechinaweather.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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

	protected void showLoadingAnimation() {
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		if(!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	protected void hideLoadingAnimation() {
		Log.e("progressDialog != null", (progressDialog != null) + "");
		Log.e("isShowing()", (progressDialog.isShowing()) + "");

		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
