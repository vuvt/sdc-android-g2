package com.nikmesoft.android.nearfood;

import java.util.ArrayList;

import com.nikmesoft.android.nearfood.helpers.FileHelper;
import com.nikmesoft.android.nearfood.helpers.SharedPreferencesHelper;
import com.nikmesoft.android.nearfood.models.User;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MyApplication extends Application {
	public static String PACKAGE_NAME;
	public static String FOLDER_NAME;
	public static int API_VERSION;
	public static int LOCATION_UPDATE_TIME;
	public static int LOCATION_UPDATE_DISTANCE;
	public static int SEARCH_RADIUS;
	public static final int MALE = 0;
	public static final int FEMALE =1;
	public static final ArrayList<CheckBox> checkboxs = new ArrayList<CheckBox>();
	private Context context;
	public static  String contentSearch = null;
	public static double distance = 8235;
	public static int distanceByKms = 0;
	public static User USER_CURRENT;
	public static double LONGITUDE = 108.149665; // DH BK DN is location default
	public static double LATITUDE = 16.074641; //
	public static boolean isSwitchTab = false, isSwitchTabLogin = false,isSwitchTabLoginChild = false;
	public static TabHost tabHost;
	public static int countCheckin = 0;
	public static Button btn_Login;
	public static int tabCurrent = 0;
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
		CheckBox checkbox = new CheckBox(this);
		checkbox.setChecked(true);
		checkboxs.add(checkbox);
		checkboxs.add(new CheckBox(this));
		checkboxs.add(new CheckBox(this));
		checkboxs.add(new CheckBox(this));
	}
}