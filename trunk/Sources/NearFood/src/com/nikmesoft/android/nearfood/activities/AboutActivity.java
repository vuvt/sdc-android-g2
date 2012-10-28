package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {

	private WebView webAbout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		webAbout = (WebView) findViewById(R.id.webabout);
		webAbout.loadUrl("file:///android_asset/about.html");
	}

}
