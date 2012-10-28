package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HelpActivity extends Activity {

	private WebView help;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		help = (WebView) findViewById(R.id.web_help);
		help.loadUrl("file:///android_asset/help.html");
		
	}

}
