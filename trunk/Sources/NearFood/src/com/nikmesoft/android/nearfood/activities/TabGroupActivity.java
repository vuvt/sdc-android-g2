package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import com.nikmesoft.android.nearfood.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;

@SuppressWarnings("deprecation")
public class TabGroupActivity extends ActivityGroup {
	private ArrayList<String> nameActivitys;
	private LocalActivityManager localActivityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nameActivitys = new ArrayList<String>();
		localActivityManager = getLocalActivityManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_tab_group, menu);
		return true;
	}

	@Override
	public void finishFromChild(Activity child) {
		// TODO Auto-generated method stub
		int countActivity = nameActivitys.size() - 1;
		if (countActivity < 1) {
			finish();
			return;
		}
		Window window = localActivityManager.destroyActivity(
				nameActivitys.get(countActivity), true);
		if (window != null) {
			nameActivitys.remove(countActivity);
			countActivity--;
			Activity activityPrevious = localActivityManager
					.getActivity(nameActivitys.get(countActivity));
			Window windowCurrent = localActivityManager.startActivity(
					nameActivitys.get(countActivity),
					activityPrevious.getIntent());
			setContentView(windowCurrent.getDecorView());
		}
	}

	public void startNewActivity(String id, Intent intent) {
		Window windowCurrent = localActivityManager.startActivity(id,
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (windowCurrent != null) {
			nameActivitys.add(id);
			setContentView(windowCurrent.getDecorView());
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (nameActivitys.size() > 1) {
			Log.d("Indexxxxxxx", String.valueOf(nameActivitys.size()));
			Activity topActivity = getLocalActivityManager().getActivity(
					nameActivitys.get(nameActivitys.size() - 1));
			topActivity.finish();
		} else
			super.onBackPressed();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
