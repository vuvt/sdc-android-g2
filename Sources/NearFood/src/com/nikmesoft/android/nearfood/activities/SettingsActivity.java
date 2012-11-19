package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;
import java.util.Set;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.xml;
import com.nikmesoft.android.nearfood.models.User;

public class SettingsActivity extends BaseActivity {
	
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
		if(MyApplication.USER_CURRENT!=null){
			login.setVisibility(View.VISIBLE);
			not_login.setVisibility(View.GONE);
			}
		else {
			login.setVisibility(View.GONE);
			not_login.setVisibility(View.VISIBLE);
			}
			//setContentView(R.layout.activity_settings);
	}
	public void OnClickProfile(View v){
		Intent intent = new Intent();
		intent.setClass(this, ProfileActivity.class);
		startActivity(intent);
	}
	public void OnClickAbout(View v){
/*		
		View view =inflater.inflate(R.layout.web_about_view, null);*/
/*		AlertDialog.Builder al = new AlertDialog.Builder(SettingsActivity.this);
		LayoutInflater inflater= this.getLayoutInflater();		
		al.setTitle("About");
		webAbout.loadUrl("file:///android_asset/about.html");
		al.setView(inflater.inflate(R.layout.web_about_view, null));
		al.setPositiveButton("OK", null);
		al.show();*/
/*		
		Intent intent = new Intent();
		intent.setClass(this, AboutActivity.class);
		startActivity(intent);*/
		/*AlertDialog.Builder al = new AlertDialog.Builder(SettingsActivity.this);
		LayoutInflater inflater= this.getLayoutInflater();
		al.setView(inflater.inflate(R.layout.about_view, null));
		al.setPositiveButton("OK", null);
		al.show();*/
		SettingsTabGroupActivity parent = (SettingsTabGroupActivity)getParent();
		parent.startNewActivity(AboutActivity.class.getSimpleName(), new Intent(this,AboutActivity.class));
	}
	public void OnClicklogin(View v){
		Intent intents = new Intent();
		intents.setClass(this, LoginActivity.class);
		startActivity(intents);
	}

	public void OnClickLogout(View v){
		Intent intents = new Intent();
		intents.setClass(this, LoginActivity.class);
		startActivity(intents);
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
		Intent intents = new Intent();
		intents.setClass(this, RegisterActivity.class);
		startActivity(intents);
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
}
