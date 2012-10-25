package com.nikmesoft.android.nearfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nikmesoft.android.nearfood.R;

public class FirstUseActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstuse);
		init();
	}

	private void init() {

	}

	public void onClickRegister(View v) {
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void onClickNotNow(View v) {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	//Test login - hoan thanh thi xoa di
	public void onClickLogin(View v) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void onClickProfile(View v) {
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
	}
}
