package com.nikmesoft.android.nearfood.activities;


import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.Utilities;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

	private EditText new_pass,old_pass,conf_pass;
	private ProgressDialog dialog;
	private String newpass,oldpass,condpass;
	private WScall loader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		init();
	}
	private void init(){
		new_pass=(EditText) findViewById(R.id.edtNew_Password);
		old_pass=(EditText) findViewById(R.id.edtOld_Password);
		conf_pass=(EditText) findViewById(R.id.edtConf_Password);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...please wait !");
	}
	public void onClickSave(View v){
		if(new_pass.getText().toString().length()==0 && old_pass.getText().toString().length()==0 && conf_pass.getText().toString().length()==0)
			Toast.makeText(getApplicationContext(), "Please enter information!", Toast.LENGTH_LONG).show();
		else 
		   if(!MyApplication.USER_CURRENT.getPassword().equals(CommonUtil.convertToMD5(old_pass.getText().toString())))
			   Toast.makeText(getApplicationContext(), "Please enter old password!", Toast.LENGTH_LONG).show();
		   else
			   if(!conf_pass.getText().toString().equals(new_pass.getText().toString()))
				   Toast.makeText(getApplicationContext(), "Please check confirm password!", Toast.LENGTH_LONG).show();
			   else 
				   if (loader == null || loader.isCancelled()
							|| loader.getStatus() == Status.FINISHED) {
						loader = new WScall();
						loader.execute(new_pass.getText().toString().trim());
			   		}
	}
	public void onClickCancel(View v){
		this.finish();
	}
	private class WScall extends AsyncTask<String, Integer, Object>{

		@Override
		protected Object doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onCancelled(Object result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
}