package com.nikmesoft.android.nearfood.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.components.CustomItemizedOverlay;
import com.nikmesoft.android.nearfood.utils.CommonUtil;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

public class SearchMapActivity extends MapActivity implements LocationListener{
	private MapView mapView;
	private MapController mc;
	private GeoPoint currentPoint;
	private LocationManager lm;
	private Drawable IC_MAP_PIN_NORMAL;
	String NamePlace, AddressPlace;
	final Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);
        init();
    }
    final Runnable mUpdateResults = new Runnable() {
		public void run() {
			showCurrentLocation(currentPoint);
		}
	};

	private void init() {
		Intent intent = getIntent();
		currentPoint = new GeoPoint(intent.getIntExtra("Latitude", 16000000), 
				intent.getIntExtra("Longitude", 108000000));
		NamePlace = intent.getStringExtra("NamePlace");
		AddressPlace = intent.getStringExtra("AddressPlace");
		IC_MAP_PIN_NORMAL = getResources().getDrawable(
				R.drawable.ic_map_pin_normal);
		IC_MAP_PIN_NORMAL.setBounds(0, 0,
				IC_MAP_PIN_NORMAL.getIntrinsicWidth(),
				IC_MAP_PIN_NORMAL.getIntrinsicHeight());
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		mc.setZoom(16);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MyApplication.LOCATION_UPDATE_TIME,
				MyApplication.LOCATION_UPDATE_DISTANCE, this);
		mHandler.post(mUpdateResults);
		CommonUtil.toastNotify(this, "Waiting for location");
		new Thread() {
			public void run() {
				while (true) {
					if (lm != null) {
						Location loc = lm
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (loc != null) {
							currentPoint = new GeoPoint(
									(int) (loc.getLatitude() * 1E6),
									(int) (loc.getLongitude() * 1E6));
							mHandler.post(mUpdateResults);
						}
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}.start();
	}

	private void showCurrentLocation(GeoPoint srcGeoPoint) {
		CustomItemizedOverlay overlayItem = new CustomItemizedOverlay(this,
				IC_MAP_PIN_NORMAL);
		OverlayItem item = new OverlayItem(srcGeoPoint, NamePlace,
				AddressPlace);
		overlayItem.addItem(item);
		mapView.getOverlays().add(overlayItem);
		mc.animateTo(srcGeoPoint);

	}

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		final Location loc = location;
		if (location != null) {
			CommonUtil.toastNotify(this, "Updating new location");
			Thread t = new Thread() {
				public void run() {
					int lat = (int) (loc.getLatitude() * 1E6);
					int lng = (int) (loc.getLongitude() * 1E6);
					currentPoint = new GeoPoint(lat, lng);
					if (currentPoint != null) {
						mHandler.post(mUpdateResults);

					}
				}
			};
			t.start();

		}
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (currentPoint != null) {
			mc.animateTo(currentPoint);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		lm.removeUpdates(this);
		super.onDestroy();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	public void onClickBack(View v){
		onBackPressed();
	}
}
