package com.nikmesoft.android.nearfood.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

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
	}
}
