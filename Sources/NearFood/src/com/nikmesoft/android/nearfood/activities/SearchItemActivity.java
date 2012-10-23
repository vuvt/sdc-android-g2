package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.layout;
import com.nikmesoft.android.nearfood.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class SearchItemActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_search_item, menu);
		return true;
	}

	public void onClickBack(View v) {

	}

	public void onClickFacebook(View v) {

	}

	public void onClickFavorite(View v) {

	}

	public void onClickLike(View v) {

	}
}
