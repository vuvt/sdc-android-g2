package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTabs();
	}

	private void initTabs() {
		addTab(R.drawable.ic_tab_search, SearchActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_checkin, CheckInActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_favorites, FavoritesActivity.class,
				R.layout.tab_item);
		addTab(R.drawable.ic_tab_settings, SettingsActivity.class,
				R.layout.tab_item);
	}

	private void addTab(int drawableId, Class<?> c, int layoutID) {
		TabHost tabHost = getTabHost();
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
