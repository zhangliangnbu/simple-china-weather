package com.tech.heathcilff.simplechinaweather.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tech.heathcilff.simplechinaweather.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

	private TextView textView;
	private Handler handler;
	private static final int OK = 1;
	private static final int ERROR = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.data);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == OK) {
					textView.setText((CharSequence) msg.obj);
				} else {
					textView.setText("request failed");
				}
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		load();
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
			if(conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuffer sb = new StringBuffer();
				String temp;
				while((temp=br.readLine()) != null) {
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
}
