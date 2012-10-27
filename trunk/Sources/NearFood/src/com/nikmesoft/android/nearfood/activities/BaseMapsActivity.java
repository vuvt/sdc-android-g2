package com.nikmesoft.android.nearfood.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import com.google.android.maps.MapActivity;

public abstract class BaseMapsActivity extends MapActivity {

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
