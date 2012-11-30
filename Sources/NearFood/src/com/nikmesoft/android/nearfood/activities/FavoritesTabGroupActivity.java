package com.nikmesoft.android.nearfood.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FavoritesTabGroupActivity extends TabGroupActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		startNewActivity(FavoritesActivity.class.getSimpleName(), new Intent(this, FavoritesActivity.class));
	}


}
