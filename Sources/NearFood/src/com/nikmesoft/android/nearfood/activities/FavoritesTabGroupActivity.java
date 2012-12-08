package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.MyApplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

public class FavoritesTabGroupActivity extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("dadadsdasdsdasddasd", "dad");
		startNewActivity(FavoritesActivity.class.getSimpleName(), new Intent(this, FavoritesActivity.class));		
	}

}
