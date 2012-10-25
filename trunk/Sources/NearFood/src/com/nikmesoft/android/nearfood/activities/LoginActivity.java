package com.nikmesoft.android.nearfood.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;

public class LoginActivity extends BaseActivity {

	private EditText edtEmail, edtPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
	}
	
	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void onClickForgotPassword(View v) {
		Intent intent = new Intent(this, ForgotPasswordActivity.class);
		startActivity(intent);
	}
	
	public void onClickRegister(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	
	public void onClickLogin(View v) {
		if("".equals(edtEmail.getText().toString().trim()) || "".equals(edtPassword.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
		} else if(!checkValidateEmail(edtEmail.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Wrong email address format!", Toast.LENGTH_LONG).show();
		} else {
			//TODO ok nef
			Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onClickCancel(View v) {
		//TODO vo main ma ko login
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
