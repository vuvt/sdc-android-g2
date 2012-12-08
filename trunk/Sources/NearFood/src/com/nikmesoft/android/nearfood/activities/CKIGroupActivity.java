package com.nikmesoft.android.nearfood.activities;

import com.google.android.maps.GeoPoint;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.binding.ExtraBinding;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.PointMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class CKIGroupActivity extends TabGroupActivity{
	Intent intent;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!MyApplication.isSwitchTab){
			startNewActivity(CKIMainActivity.class.getSimpleName(), new Intent(this,
					CKIMainActivity.class));
		}
		 BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				MyApplication.isSwitchTab = false;
				//CKIGroupActivity.this.getParent().
				intent = new Intent();
				intent.setClass(CKIGroupActivity.this, CKICheckInActivity.class);
				Place place = new Place();
				Bundle bundle = arg1.getBundleExtra("bundlePlace");
				place.setId(bundle.getInt("idPlace"));
				place.setNamePlace(bundle.getString("namePlace"));
				place.setAddress(bundle.getString("addressPlace"));
				place.setPhoneNumber(bundle.getString("phonePlace"));
				place.setDescription(bundle.getString("descriptionPlace"));
				place.setReferenceKey(bundle.getString("referenceKeyPlace"));
				GeoPoint point = new GeoPoint(bundle.getInt("latitudePlace"), bundle.getInt("longitudePlace"));
				place.setMapPoint(point);
				Bundle bundle1 = new Bundle();
				bundle1.putSerializable(ExtraBinding.PLACE_BINDING,
						place);
				intent.putExtras(bundle1); 
				
				startNewActivity(CKICheckInActivity.class.getSimpleName() + "#$#$#" + place.getName() + String.valueOf(++MyApplication.countCheckin),intent);
			}   
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.nikmesoft.android.nearfood.activities.DATA_BROADCAST");
			registerReceiver(receiver, filter);
	}
	
}
