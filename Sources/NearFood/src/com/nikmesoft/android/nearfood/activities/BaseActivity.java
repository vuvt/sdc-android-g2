package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.binding.AppBinding;
import com.nikmesoft.android.nearfood.helpers.SharedPreferencesHelper;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {
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
