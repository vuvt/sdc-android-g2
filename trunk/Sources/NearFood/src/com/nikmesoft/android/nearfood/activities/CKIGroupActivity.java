package com.nikmesoft.android.nearfood.activities;

import android.content.Intent;
import android.os.Bundle;

public class CKIGroupActivity extends TabGroupActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startChildActivity(CKIMainActivity.class.getSimpleName(), new Intent(this,
				CKIMainActivity.class));
	}
}
