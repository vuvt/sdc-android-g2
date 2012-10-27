package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.helpers.SharedPreferencesHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class SplashActivity extends BaseActivity {
	private Handler handler = new Handler();
	private boolean isTouched = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		handler.postDelayed(action, 3000);
	}

	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			stop();
		}
		return true;
	}

	private Runnable action = new Runnable() {
		public void run() {
			stop();
		}
	};

	private void stop() {
		if (!isTouched) {
			isTouched = true;
			Intent intent = new Intent();
			if (SharedPreferencesHelper.getInstance().isFirstUse()) {
				intent.setClass(SplashActivity.this, FirstUseActivity.class);
				// sharedPreferencesHelper.setFirstUse(false);
			} else {
				intent.setClass(SplashActivity.this, MainActivity.class);
			}
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onStop() {
		handler.removeCallbacks(action);
		super.onStop();
	}
}
