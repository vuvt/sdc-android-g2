package com.nikmesoft.android.nearfood.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;



import com.nikmesoft.android.nearfood.R;

public class CheckInActivity extends BaseMapsActivity {
	EditText edt_search;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setContentView(R.layout.activity_checkin);
		edt_search = (EditText) findViewById(R.id.edt_search);
		edt_search
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						return false;
					}
				});
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void actionSearch(View v) {
		performSearch();
	}


	private void performSearch() {
	}

	
}
