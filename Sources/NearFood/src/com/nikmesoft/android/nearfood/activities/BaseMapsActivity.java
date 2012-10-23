package com.nikmesoft.android.nearfood.activities;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.nikmesoft.android.nearfood.binding.AppBinding;
import com.nikmesoft.android.nearfood.helpers.SharedPreferencesHelper;

public abstract class BaseMapsActivity extends MapActivity {

	protected int apiVersion;
	protected int screen_width;
	protected int screen_height;
	protected SharedPreferencesHelper sharedPreferencesHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void init() {
		apiVersion = android.os.Build.VERSION.SDK_INT;
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		screen_width = size.x;
		screen_height = size.y;
		AppBinding.PACKAGE_NAME = getApplicationContext().getPackageName();

		// init helpers
		sharedPreferencesHelper = new SharedPreferencesHelper(this);
	}

}
