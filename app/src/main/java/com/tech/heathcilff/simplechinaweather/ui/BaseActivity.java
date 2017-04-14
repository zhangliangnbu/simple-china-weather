package com.tech.heathcilff.simplechinaweather.ui;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 *
 * Created by zhangliang on 2017/2/14.
 */

public abstract class BaseActivity extends AppCompatActivity {
	protected ProgressDialog progressDialog;

	protected void showLoadingAnimation() {
		if(progressDialog == null) {
			progressDialog = new ProgressDialog(this);
		}
		if(!progressDialog.isShowing()) {
			progressDialog.show();
			Log.d("show==", System.currentTimeMillis() + "");
		}
	}

	protected void hideLoadingAnimation() {
		if(progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
