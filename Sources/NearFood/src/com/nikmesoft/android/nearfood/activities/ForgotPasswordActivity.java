package com.nikmesoft.android.nearfood.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;

public class ForgotPasswordActivity extends BaseActivity {
	
	private EditText edtEmail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		init();
	}

	private void init() {
		edtEmail = (EditText) findViewById(R.id.edtEmail);
	}
	
	public void onClickResetPassword(View v) {
		if("".equals(edtEmail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Please enter your email!", Toast.LENGTH_LONG).show();
		} else if(!checkValidateEmail(edtEmail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Wrong email address format!", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(), "Email the instructions to retrieve your password be sent to "+edtEmail.getText().toString().trim()+", please check your email for instructions to retrieve your password!", Toast.LENGTH_LONG).show();
			setResult(RESULT_CANCELED);
			finish();
		}
	}
	
	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * check validate email address format
	 * @param email_address
	 * @return true if valid, false if invalid
	 */
	public boolean checkValidateEmail(String email_address){
		return email_address.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
}
