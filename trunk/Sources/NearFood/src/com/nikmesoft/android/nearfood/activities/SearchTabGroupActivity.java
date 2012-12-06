package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SearchTabGroupActivity extends TabGroupActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.isSwitchTab = false;
        startNewActivity(SearchActivity.class.getSimpleName(), new Intent(this, SearchActivity.class));
    }
}
