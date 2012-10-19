package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;

import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}

	private void init() {

	}

	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
}
