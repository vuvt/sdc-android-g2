package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;
import java.util.Set;
import java.util.prefs.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.xml;

public class SettingsActivity extends BaseActivity {
	
	private static final String key_log="login";
	private ListView myListView,myLi;
	private ArrayAdapter<String> myAdapter,myad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// neu chua login
		setContentView(R.layout.activity_settings);
		/*neu da login
		 * setContentView(R.layout.activity_settings_login)
		 * */
	}
	public void OnClickProfile(View v){
		Intent intent = new Intent();
		intent.setClass(this, ProfileActivity.class);
		startActivity(intent);
	}
	public void OnClickAbout(View v){

		AlertDialog.Builder al = new AlertDialog.Builder(SettingsActivity.this);
		LayoutInflater inflater = this.getLayoutInflater();
		al.setTitle("About");
		al.setView(inflater.inflate(R.layout.about_view, null));
		al.setPositiveButton("OK", null);
		al.show();
	}
	public void OnClicklogin(View v){
/*		Intent intents = new Intent();
		intents.setClass(this, LoginActivity.class);
		startActivity(intents);*/
	}

	public void OnClicklogout(View v){
/*	Intent intents = new Intent();
	intents.setClass(this, LoginActivity.class);
	startActivity(intents);*/
	}
	public void OnClickFeedback(View v){
		//---------------
	}
	public void OnClickHelp(View v){
		//------------
	}
	
	public void OnClickRegister(View v){
		Intent intents = new Intent();
		intents.setClass(this, RegisterActivity.class);
		startActivity(intents);
	}
	public void OnClickChangepass(View v){
		AlertDialog.Builder al = new AlertDialog.Builder(SettingsActivity.this);
		LayoutInflater inflater = this.getLayoutInflater();
		al.setTitle("Change password");
		al.setView(inflater.inflate(R.layout.change_password, null));
		al.setPositiveButton("OK",null);
        al.setNegativeButton("Cancel",null);
		al.show();
	}
}
