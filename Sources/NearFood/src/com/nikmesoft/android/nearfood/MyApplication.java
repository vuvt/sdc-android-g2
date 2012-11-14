package com.nikmesoft.android.nearfood;

import com.nikmesoft.android.nearfood.helpers.FileHelper;
import com.nikmesoft.android.nearfood.helpers.SharedPreferencesHelper;
import com.nikmesoft.android.nearfood.models.User;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
	public static String PACKAGE_NAME;
	public static String FOLDER_NAME;
	public static int API_VERSION;
	public static int LOCATION_UPDATE_TIME;
	public static int LOCATION_UPDATE_DISTANCE;
	public static int SEARCH_RADIUS;
	public static final int MALE = 0;
	public static final int FEMALE =1;
	
	private Context context;
	
	public static User USER_CURRENT;

	@Override
	public void onCreate() {
		super.onCreate();
		initVariables();
		initSingletons();
	}

	protected void initSingletons() {
		// init Helpers
		SharedPreferencesHelper.initInstance(context);
		FileHelper.initInstance(context);

	}

	protected void initVariables() {
		context = getApplicationContext();
		PACKAGE_NAME = getPackageName();
		FOLDER_NAME = "SFood";
		API_VERSION = android.os.Build.VERSION.SDK_INT;
		// GPS
		LOCATION_UPDATE_TIME = 30 * 1000;
		LOCATION_UPDATE_DISTANCE = 100;
		SEARCH_RADIUS = 500;
	}
}