package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.binding.ExtraBinding;
import com.nikmesoft.android.nearfood.models.Place;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class SettingsTabGroupActivity extends TabGroupActivity {
	Intent intent;
	int REQUEST_LOGIN = 200;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(!MyApplication.isSwitchTabLogin){
			startNewActivity(SettingsActivity.class.getSimpleName(), new Intent(this,
					SettingsActivity.class));
		}
		 BroadcastReceiver receiver = new BroadcastReceiver() {
			 
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(SettingsTabGroupActivity.this, LoginActivity.class);
				getParent().startActivityForResult(intent, 1231);
				
			}    
			  
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.nikmesoft.android.nearfood.activities.LOGIN_BROADCAST");
			registerReceiver(receiver, filter);
	}
	
}
