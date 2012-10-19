package com.nikmesoft.android.nearfood.helpers;

import com.nikmesoft.android.nearfood.binding.SharedPreferencesBinding;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

	public SharedPreferencesHelper(Context context) {
		super();
		sharedPreferences = context.getSharedPreferences(
				SharedPreferencesBinding.PREFS, Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	// check is first use
	public boolean isFirstUse() {

		return sharedPreferences.getBoolean(SharedPreferencesBinding.FIRSTUSE,
				true);
	}

	// set first use
	public void setFirstUse(boolean value) {
		editor.putBoolean(SharedPreferencesBinding.FIRSTUSE, value);
		editor.commit();
	}
}
