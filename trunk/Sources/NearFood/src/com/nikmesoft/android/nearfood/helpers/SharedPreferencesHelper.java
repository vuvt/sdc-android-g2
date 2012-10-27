package com.nikmesoft.android.nearfood.helpers;

import com.nikmesoft.android.nearfood.binding.SharedPreferencesBinding;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private static SharedPreferencesHelper instance;

	public SharedPreferencesHelper(Context context) {
		super();
		sharedPreferences = context.getSharedPreferences(
				SharedPreferencesBinding.PREFS, Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public static void initInstance(Context context) {
		if (instance == null) {
			// Create the instance
			instance = new SharedPreferencesHelper(context);
		}
	}

	public static SharedPreferencesHelper getInstance() {
		// Return the instance
		return instance;
	}

	public boolean isFirstUse() {

		return sharedPreferences.getBoolean(SharedPreferencesBinding.FIRSTUSE,
				true);
	}

	public void setFirstUse(boolean value) {
		editor.putBoolean(SharedPreferencesBinding.FIRSTUSE, value);
		editor.commit();
	}

	public int getNumofUses() {

		return sharedPreferences.getInt(SharedPreferencesBinding.NUMOFUSES, 0);

	}

	public void updateNumofUses() {

		editor.putInt(SharedPreferencesBinding.NUMOFUSES, getNumofUses() + 1);
		editor.commit();
	}
}