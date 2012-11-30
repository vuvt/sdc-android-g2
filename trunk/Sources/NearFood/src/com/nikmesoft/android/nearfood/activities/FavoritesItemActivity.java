package com.nikmesoft.android.nearfood.activities;

import com.google.android.maps.MapActivity;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

public class FavoritesItemActivity extends MapActivity {
	private int imgIndex=1;
	private ViewFlipper flipper,map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites_item);
		flipper = (ViewFlipper) findViewById(R.id.details);
		map = (ViewFlipper) findViewById(R.id.map);
	}
	public void onClickBack(View v){
		this.finishFromChild(getParent());
	}
	public void onClickMap(View v){
		AnimationFactory.flipTransition(flipper, FlipDirection.RIGHT_LEFT);
		ImageButton btnListMap = (ImageButton) v.findViewById(R.id.bt_listmap);
		/*if (imgIndex == 1) {
			imgIndex = 2;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_list));
		} else {
			imgIndex = 1;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_map));
		}*/
		AnimationFactory.flipTransition(map, FlipDirection.RIGHT_LEFT);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
