package com.nikmesoft.android.nearfood.activities;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

public class FavoritesItemActivity extends MapActivity {
	private int imgIndex=1;
	private ViewFlipper flipper,map;
	private TextView name,address,description;
	private Place place;
	private MapView mapview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_favorites_item);
		flipper = (ViewFlipper) findViewById(R.id.details);
		map = (ViewFlipper) findViewById(R.id.map);
		name = (TextView) findViewById(R.id.favorites_name_item);
		address = (TextView) findViewById(R.id.address_information);
		description = (TextView) findViewById(R.id.description_information);
		mapview = (MapView) findViewById(R.id.mapviewfavorites_item);
		init();
		
	}
	private void init(){		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundlePlace");
		place = (Place) bundle.getSerializable("place");
		name.setText(place.getName());
		address.setText(place.getAddress());
		description.setText(place.getDescription());
	}
	public void onClickBack(View v){
		this.finishFromChild(getParent());
	}
	public void onClickMap(View v){
		AnimationFactory.flipTransition(flipper, FlipDirection.RIGHT_LEFT);
		ImageButton btnListMap = (ImageButton) v.findViewById(R.id.bt_listmap);
		AnimationFactory.flipTransition(map, FlipDirection.RIGHT_LEFT);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
