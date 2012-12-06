package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import com.nikmesoft.android.nearfood.MyApplication;


import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

@SuppressWarnings("deprecation")
public class TabGroupActivity extends ActivityGroup {
	protected ArrayList<String> nameActivitys;
	private LocalActivityManager localActivityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nameActivitys = new ArrayList<String>();
		localActivityManager = getLocalActivityManager();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Activity activityCurrent = localActivityManager
				.getActivity(nameActivitys.get(nameActivitys.size() - 1));
		if (activityCurrent.getClass().getSuperclass().getName()
				.equals(BaseActivity.class.getName())){
			((BaseActivity)activityCurrent).onActivityResult(requestCode, resultCode, data);
			((BaseActivity)activityCurrent).onGroupActivityResult(requestCode, resultCode, data);
		}else if(activityCurrent.getClass().getSuperclass().getName()
						.equals(BaseMapsActivity.class.getName())){
			((BaseMapsActivity)activityCurrent).onActivityResult(requestCode, resultCode, data);
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
			int i ;
			String str1=null,str2=null;
			boolean istrue = false;
			for(i = 0;i< nameActivitys.size()-1; i++){
				 str1 = (nameActivitys.get(i).trim()).substring(0, 10);
				 str2 = (nameActivitys.get(i+1).trim()).substring(0, 10);
				 Log.d("2 em  : ", str1 + " and " + str2);
				if(str1.equals(str2)){istrue = true; break;}
			}
			if(istrue){
				
				ArrayList<String> tmp = new ArrayList<String>(nameActivitys);
				Log.d("2 em 1 : ", str1 + " and " + str2 + " + " + tmp.get(0));
				nameActivitys = new ArrayList<String>();
				for(int j=0;j<(i+1); j++)nameActivitys.add(tmp.get(j));
				Log.d("length", String.valueOf(nameActivitys.size()));
			if(i>0){
				Activity topActivity = getLocalActivityManager().getActivity(
						nameActivitys.get(i));
				
				topActivity.finish();
			}
			else{
				Activity topActivity = getLocalActivityManager().getActivity(
						nameActivitys.get(nameActivitys.size() - 1));
				topActivity.finish();
				}
			}
			
			else{
			Activity topActivity = getLocalActivityManager().getActivity(
					nameActivitys.get(nameActivitys.size() - 1));
			topActivity.finish();
			}
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
