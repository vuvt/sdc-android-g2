package com.nikmesoft.android.nearfood.activities;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.nikmesoft.android.nearfood.R;

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
	
	public void onClickDatePicker(View v) {
		//new DatePickerFragment().showDatePickerDialog(v);
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
		//Toast.makeText(getApplicationContext(), "nhan ne", Toast.LENGTH_LONG).show();
	}
}
