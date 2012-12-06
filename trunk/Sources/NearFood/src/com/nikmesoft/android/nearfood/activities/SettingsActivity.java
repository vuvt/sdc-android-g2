package com.nikmesoft.android.nearfood.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;

public class SettingsActivity extends BaseActivity {
	
	private static final int REQUEST_LOGIN = 100;
	private static final int REQUEST_REGISTER = 101;
	
	private static final String key_log="login";
	private ListView myListView,myLi;
	private LinearLayout login,not_login;
	private ArrayAdapter<String> myAdapter,myad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		login=(LinearLayout) findViewById(R.id.layout_Login);
		not_login = (LinearLayout) findViewById(R.id.layout_notLogin);

		checkLoginedOrNotLogin();
	}
	public void OnClickProfile(View v){
		Intent intent = new Intent();
		intent.setClass(this, ProfileActivity.class);
		startActivity(intent);
		
//		SettingsTabGroupActivity parent = (SettingsTabGroupActivity)getParent();
//		parent.startNewActivity(ProfileActivity.class.getSimpleName(), new Intent(this,ProfileActivity.class));
	}
	public void OnClickAbout(View v){
		SettingsTabGroupActivity parent = (SettingsTabGroupActivity)getParent();
		parent.startNewActivity(AboutActivity.class.getSimpleName(), new Intent(this,AboutActivity.class));
	}
	
	public void OnClicklogin(View v){
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		getParent().startActivityForResult(intent, REQUEST_LOGIN);
	}
	
	public void OnClickLogout(View v){
		
		new AlertDialog.Builder(getParent())
        .setMessage("Are you sure you want to logout?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	MyApplication.USER_CURRENT = null;
            	MyApplication.btn_Login.setVisibility(View.VISIBLE);
        		Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
        		checkLoginedOrNotLogin();
            }
        })
        .setNegativeButton("No", null)
        .show();
		
	}
	public void OnClickFeedback(View v){
		try {
	        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	        String[] recipients = new String[]{"g2.bkdn@gmail.com"};
	        String[] ccList = {"dvl101bkdn@gmail.com"};// email dang ky..
	        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
	        emailIntent.putExtra(android.content.Intent.EXTRA_BCC, ccList);
	        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback for sFood v1.0!");
	        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
	        emailIntent.setType("text/plain");
	        startActivity(Intent.createChooser(emailIntent,"send mail..."));
	        finish();
        } catch (Exception e) {
        }		
	}
	public void OnClickHelp(View v){
		SettingsTabGroupActivity parent = (SettingsTabGroupActivity)getParent();
		parent.startNewActivity(HelpActivity.class.getSimpleName(), new Intent(this,HelpActivity.class));
	}
	
	public void OnClickRegister(View v){
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		intent.putExtra("FromActivity", "Settings");
		getParent().startActivityForResult(intent, REQUEST_REGISTER);
	}
	public void OnClickChangepass(View v){
		/*AlertDialog.Builder al = new AlertDialog.Builder(SettingsActivity.this);
		LayoutInflater inflater = this.getLayoutInflater();
		al.setTitle("Change password");
		al.setView(inflater.inflate(R.layout.change_password, null));
		al.setPositiveButton("OK",null);
        al.setNegativeButton("Cancel",null);
		al.show();*/
		SettingsTabGroupActivity parent = (SettingsTabGroupActivity)getParent();
		parent.startNewActivity(ChangePasswordActivity.class.getSimpleName(), new Intent(this,ChangePasswordActivity.class));
	}
	
	private void checkLoginedOrNotLogin() {
		if (MyApplication.USER_CURRENT != null) {
			login.setVisibility(View.VISIBLE);
			not_login.setVisibility(View.GONE);
		} else {
			login.setVisibility(View.GONE);
			not_login.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onGroupActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_LOGIN) {
			if(resultCode == RESULT_OK) {
				checkLoginedOrNotLogin();
			} else {
				//nothing...
			}
		} else if(requestCode == REQUEST_REGISTER) {
			if(resultCode == RESULT_OK) {
				checkLoginedOrNotLogin();
			}
		}
	}
}
