package com.tech.heathcilff.simplechinaweather.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tech.heathcilff.androidlib.utils.Pop;
import com.tech.heathcilff.simplechinaweather.R;
import com.tech.heathcilff.simplechinaweather.engine.Intents;
import com.tech.heathcilff.simplechinaweather.entity.AllWeatherForecast;
import com.tech.heathcilff.simplechinaweather.entity.BaseForecast;
import com.tech.heathcilff.simplechinaweather.entity.CityInfo;
import com.tech.heathcilff.simplechinaweather.helper.CityManagementHelper;
import com.tech.heathcilff.simplechinaweather.net.WrappedNetApi;
import com.tech.heathcilff.simplechinaweather.observer.NetCallbackObserver;
import com.tech.heathcilff.simplechinaweather.ui.binding.ActivityNavigationBinding;
import com.tech.heathcilff.simplechinaweather.ui.binding.AppBarNavigationBinding;
import com.tech.heathcilff.simplechinaweather.ui.binding.ContentNavigationBinding;
import com.tech.heathcilff.simplechinaweather.viewmodel.AllWeatherModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class NavigationActivity extends BaseActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	private ActivityNavigationBinding binding;
	private AppBarNavigationBinding barContentBinding;
	private ContentNavigationBinding contentBinding;
	private AllWeatherModel allWeatherModel;

	private CompositeDisposable compositeDisposable;
	private CityInfo currentCity = new CityInfo();
	private CityInfo localCity = new CityInfo();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// binding
		binding = DataBindingUtil.setContentView(this, R.layout.activity_navigation);
		barContentBinding = binding.includeAppBarNavigation;
		contentBinding = barContentBinding.includeContentNavigation;
		setSupportActionBar(barContentBinding.toolbar);

		allWeatherModel = new AllWeatherModel();
		contentBinding.setAllForecast(allWeatherModel);

		barContentBinding.fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, binding.drawerLayout, barContentBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		binding.drawerLayout.setDrawerListener(toggle);
		toggle.syncState();

		binding.navView.setNavigationItemSelectedListener(this);


		contentBinding.layoutSwipeRefresh.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
		contentBinding.layoutSwipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
		contentBinding.layoutSwipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
		contentBinding.layoutSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (currentCity.isValid()) {
					qryAllWeatherForecast(currentCity.id);
				}
			}
		});

		compositeDisposable = new CompositeDisposable();
		// // TODO: 13/04/2017 应该在开始页面定位
		qryLocalCity();
		if (CityManagementHelper.getCurrentCity().isValid()) {
			currentCity = CityManagementHelper.getCurrentCity();
			qryAllWeatherForecast(currentCity.id);
		} else {
			locationAndQryForecast();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		compositeDisposable.clear();
	}


	@Override
	public void onBackPressed() {
		if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
			binding.drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_positioning:
				locationAndQryForecast();
				return true;
			case R.id.action_share:
				Pop.toast("share");
				return true;
			case R.id.action_manage_city:
				startActivity(Intents.toCityManagement());
				return true;
			case R.id.action_settings:
				startActivity(Intents.toSettings());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}
		Pop.toast("selected a navigation item");
		binding.drawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}

	private void qryLocalCity() {
		final Location location = getLocation();
		if (location == null) return;
		Disposable d = WrappedNetApi
				.searchCity(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()))
				.subscribeWith(new NetCallbackObserver<BaseForecast>() {
					@Override
					public void onNext(BaseForecast value) {
						localCity = value.basic;
						barContentBinding.ivNearMe.setVisibility(CityManagementHelper.isEqual(currentCity, localCity) ? View.VISIBLE : View.GONE);
					}
				});
		compositeDisposable.add(d);
	}

	private void locationAndQryForecast() {
		Location location = getLocation();
		if (location != null) {
			qryAllWeatherForecast(location.getLongitude() + "," + location.getLatitude());
		} else {
			Pop.toast("无法定位");
		}
	}

	private void qryAllWeatherForecast(String city) {
		if(!contentBinding.layoutSwipeRefresh.isRefreshing()) {
			contentBinding.layoutSwipeRefresh.setRefreshing(true);
		}
		Disposable d = WrappedNetApi
				.allForecast(city)
				.subscribeWith(new DisposableObserver<AllWeatherForecast>() {
					@Override
					public void onNext(AllWeatherForecast value) {
						onDailyForecastUpdated(value);
					}

					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
						contentBinding.layoutSwipeRefresh.setRefreshing(false);
					}

					@Override
					public void onComplete() {
						contentBinding.layoutSwipeRefresh.setRefreshing(false);
					}
				});
		compositeDisposable.add(d);
	}

	private void onDailyForecastUpdated(AllWeatherForecast value) {
		currentCity = value.basic;
		setTitle(currentCity.city);
		CityManagementHelper.updateCurrentCity(currentCity);
		barContentBinding.ivNearMe.setVisibility(CityManagementHelper.isEqual(currentCity, localCity) ? View.VISIBLE : View.GONE);
		allWeatherModel.set(value);
	}

	@Nullable
	private Location getLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		// 使用省电模式
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 获得当前的位置提供者
		String provider = lm.getBestProvider(criteria, true);
		// 获得当前的位置
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Pop.toast("没有位置授权!");
			return null;
		}
		return lm.getLastKnownLocation(provider);
	}
}
