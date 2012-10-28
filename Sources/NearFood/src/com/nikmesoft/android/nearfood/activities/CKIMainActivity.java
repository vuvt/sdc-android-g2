package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.AutoCompleteAdapter;
import com.nikmesoft.android.nearfood.adapters.CheckInResultAdapter;
import com.nikmesoft.android.nearfood.binding.ExtraBinding;
import com.nikmesoft.android.nearfood.components.CustomItemizedOverlay;
import com.nikmesoft.android.nearfood.components.GetPlaceInfomation;
import com.nikmesoft.android.nearfood.components.GetPlaces;
import com.nikmesoft.android.nearfood.components.GetPlacesAutoComplete;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.CommonUtil;

public class CKIMainActivity extends BaseMapsActivity implements
		LocationListener, AdapterView.OnItemClickListener {

	ListView list;
	AutoCompleteTextView textView;
	CheckInResultAdapter checkinResultAdapter;
	MapView mapView;
	private MapController mc;
	private GeoPoint currentPoint;
	private LocationManager lm;
	final Handler mHandler = new Handler();
	private LayoutParams primaryLayoutParams;
	private final LayoutParams match_parent = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	private AutoCompleteAdapter autoCompleteAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	ArrayList<String[]> listarray;
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			showCurrentLocation(currentPoint);
		}
	};

	private void init() {

		setContentView(R.layout.activity_checkin);

		// Add data to list
		list = (ListView) findViewById(R.id.list);
		checkinResultAdapter = new CheckInResultAdapter(this,
				R.layout.activity_checkin_list_item, new ArrayList<Place>(),
				mapView);
		list.setAdapter(checkinResultAdapter);
		list.setOnItemClickListener(this);

		// Map define component
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		primaryLayoutParams = mapView.getLayoutParams();
		mc = mapView.getController();
		// mc.setCenter(new GeoPoint(108149794, 16073651));
		mc.setZoom(16);

		// AutoCompleteView
		textView = (AutoCompleteTextView) findViewById(R.id.tv_search);
		textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}
		});
		autoCompleteAdapter = new AutoCompleteAdapter(this,
				R.layout.activity_checkin_auto_complete_list_item);
		autoCompleteAdapter.setNotifyOnChange(true);
		textView.setAdapter(autoCompleteAdapter);
		textView.setOnItemClickListener(this);
		textView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				
				GetPlacesAutoComplete getplaces = new GetPlacesAutoComplete(
						textView, autoCompleteAdapter);
				getplaces.execute(textView.getText().toString());
				Log.d("Result count",
						String.valueOf(autoCompleteAdapter.getCount()));

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

		});
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MyApplication.LOCATION_UPDATE_TIME,
				MyApplication.LOCATION_UPDATE_DISTANCE, this);
		CommonUtil.toastNotify(this, "Waiting for location");
		new Thread() {
			public void run() {
				// Get the current location in start-up
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

			}
		}.start();

	}

	public void actionSearch(View v) {
		performSearch();
	}

	private void performSearch() {
		mapView.setLayoutParams(primaryLayoutParams);
		GetPlaces getplaces = new GetPlaces(textView, checkinResultAdapter,
				mapView);
		getplaces.execute(textView.toString());
	}

	public void addOverlay(OverlayItem item) {

		Drawable drawable = getResources().getDrawable(
				R.drawable.ic_map_pin_normal);
		CustomItemizedOverlay itemizedOverlay = new CustomItemizedOverlay(
				drawable, this);
		itemizedOverlay.addOverlay(item);
		mapView.getOverlays().add(itemizedOverlay);
	}

	private void showCurrentLocation(GeoPoint srcGeoPoint) {
		Drawable icon = getResources().getDrawable(
				R.drawable.ic_map_pin_current);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(),
				icon.getIntrinsicHeight());
		CustomItemizedOverlay overlayItem = new CustomItemizedOverlay(icon,
				this);
		OverlayItem item = new OverlayItem(srcGeoPoint, "Current location",
				"I'm here (" + srcGeoPoint.getLatitudeE6() / 1E6 + ","
						+ srcGeoPoint.getLongitudeE6() / 1E6 + ")");
		overlayItem.addOverlay(item);
		if (mapView.getOverlays().size() == 0)
			mapView.getOverlays().add(overlayItem);
		else
			mapView.getOverlays().set(0, overlayItem);
		mc.animateTo(srcGeoPoint);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (currentPoint != null) {
			mc.animateTo(currentPoint);
		}
	}

	@Override
	protected void onDestroy() {
		lm.removeUpdates(this);
		super.onDestroy();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// Overide implement methods

	public void onLocationChanged(Location location) {
		final Location loc = location;
		if (location != null) {
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

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		if (view.equals(list)) {
			CKIGroupActivity parent = (CKIGroupActivity) getParent();
			Intent intent = new Intent(parent, CKICheckInActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ExtraBinding.PLACE_BINDING,
					checkinResultAdapter.getItem(position));
			intent.putExtras(bundle);
			parent.startNewActivity(CKICheckInActivity.class.getSimpleName(),
					intent);
		} else if (view.equals(textView)) {
			textView.setText(autoCompleteAdapter.getItem(position).getName());
			mapView.setLayoutParams(match_parent);
			showResultAutoComplete(autoCompleteAdapter.getItem(position)
					.getReferenceKey());
		}

	}

	public void checkIn(Place place) {
		Intent intent = new Intent(this, CKICheckInActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void showResultAutoComplete(String reference) {
		ProgressDialog loading = ProgressDialog.show(this, "Loading...",
				"Please wait!");
		GetPlaceInfomation getInfo = new GetPlaceInfomation(reference);
		getInfo.execute("");
		while (!getInfo.ended) {
		}
		loading.dismiss();

	}

}
