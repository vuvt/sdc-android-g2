package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabs();
		
		getTabHost().setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				Log.d("Tabid", tabId);
				if(tabId.equals("tab2130837604")) {
					if(MyApplication.USER_CURRENT == null) {
						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);		
				        builder.setMessage("You need to be logged in to use this function"+"\n Would you like to login !")
				               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id){
				                	   Intent intent = new Intent();
				                		intent.setClass(MainActivity.this, LoginActivity.class);
				                		startActivity(intent);//ForResult(intent, REQUEST_LOGIN);
				                   }
				               })
				               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				                   public void onClick(DialogInterface dialog, int id) {
				                   }
				               });
				        builder.create().show();
					}
				}
			}
		});
	}

	private void initTabs() {
		addTab(R.drawable.ic_tab_search, SearchTabGroupActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_checkin, CKIGroupActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_favorites, FavoritesTabGroupActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_settings, SettingsTabGroupActivity.class,
				R.layout.tab_item);
		
	}

	private void addTab(int drawableId, Class<?> c, int layoutID) {
		TabHost tabHost = getTabHost();
		MyApplication.tabHost = tabHost;
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + drawableId);
		View tabIndicator = LayoutInflater.from(this).inflate(layoutID,
				getTabWidget(), false);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}
