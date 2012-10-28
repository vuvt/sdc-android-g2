package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.layout;
import com.nikmesoft.android.nearfood.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SearchTabGroupActivity extends TabGroupActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        startNewActivity(SearchActivity.class.getSimpleName(), new Intent(this, SearchActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_tab_group, menu);
        return true;
    }
}
