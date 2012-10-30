package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
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
	private ArrayAdapter<String> autoCompleteAdapter;
	private ArrayList<String> references;
	public int oldOverlay = -1;
	private Drawable IC_MAP_PIN_NORMAL;
	private Drawable IC_MAP_PIN_CHOSE;
	private Drawable IC_MAP_PIN_CURRENT;
	private Button bt_showhidelist;
	LinearLayout layout_list;

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
		IC_MAP_PIN_NORMAL = getResources().getDrawable(
				R.drawable.ic_map_pin_normal);
		IC_MAP_PIN_NORMAL.setBounds(0, 0,
				IC_MAP_PIN_NORMAL.getIntrinsicWidth(),
				IC_MAP_PIN_NORMAL.getIntrinsicHeight());
		IC_MAP_PIN_CHOSE = getResources().getDrawable(
				R.drawable.ic_map_pin_chose);
		IC_MAP_PIN_CHOSE.setBounds(0, 0, IC_MAP_PIN_CHOSE.getIntrinsicWidth(),
				IC_MAP_PIN_CHOSE.getIntrinsicHeight());
		IC_MAP_PIN_CURRENT = getResources().getDrawable(
				R.drawable.ic_map_pin_current);
		IC_MAP_PIN_CURRENT.setBounds(0, 0,
				IC_MAP_PIN_CURRENT.getIntrinsicWidth(),
				IC_MAP_PIN_CURRENT.getIntrinsicHeight());
		mapView = (MapView) findViewById(R.id.mapView);
		// Add data to list
		list = (ListView) findViewById(R.id.list);
		checkinResultAdapter = new CheckInResultAdapter(this,
				R.layout.activity_checkin_list_item, new ArrayList<Place>(),
				mapView);
		list.setAdapter(checkinResultAdapter);
		list.setOnItemClickListener(this);
		bt_showhidelist = (Button) findViewById(R.id.bt_showhidelist);
		layout_list = (LinearLayout) findViewById(R.id.layout_list);

		// Map define component

		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		// mc.setCenter(new GeoPoint(108149794, 16073651));
		mc.setZoom(16);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MyApplication.LOCATION_UPDATE_TIME,
				MyApplication.LOCATION_UPDATE_DISTANCE, this);
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
		autoCompleteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		references = new ArrayList<String>();
		autoCompleteAdapter.setNotifyOnChange(true);
		textView.setAdapter(autoCompleteAdapter);
		textView.setOnItemClickListener(this);
		textView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				GetPlacesAutoComplete getplaces;
				getplaces = new GetPlacesAutoComplete(textView,
						autoCompleteAdapter, references);

				getplaces.execute(textView.getText().toString());

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

		});

	}

	public void actionSearch(View v) {
		performSearch();
	}

	private void performSearch() {

		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		final ProgressDialog loading = new ProgressDialog(getParent());
		loading.setMessage("Loading. Please wait...");
		loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loading.setCancelable(false);
		loading.show();
		final GetPlaces getplaces = new GetPlaces(
				textView.getText().toString(), checkinResultAdapter);
		new Thread(new Runnable() {

			public void run() {

				try {

					getplaces.execute(textView.toString());
					getplaces.get();

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Overlay temp = mapView.getOverlays().get(0);
				mapView.getOverlays().clear();
				mapView.getOverlays().add(temp);
				for (int i = 0; i < checkinResultAdapter.getCount(); i++) {
					OverlayItem item = new OverlayItem(checkinResultAdapter
							.getItem(i).getMapPoint(), "", checkinResultAdapter
							.getItem(i).getName()
							+ "\n"
							+ checkinResultAdapter.getItem(i).getAddress());
					addOverlay(item);
				}
				if (checkinResultAdapter.getCount() > 0)
					mc.animateTo(checkinResultAdapter.getItem(0).getMapPoint());
				layout_list.setVisibility(View.VISIBLE);
				bt_showhidelist.setVisibility(View.VISIBLE);
				loading.dismiss();
			}
		}).start();

	}

	public void addOverlay(OverlayItem item) {

		CustomItemizedOverlay overlayItem = new CustomItemizedOverlay(this,
				IC_MAP_PIN_NORMAL);
		overlayItem.addItem(item);
		mapView.getOverlays().add(overlayItem);
		mc.animateTo(item.getPoint());
	}

	public void setChosenOverlay(int position) {
		if (oldOverlay > 0) {
			CustomItemizedOverlay normalItem = new CustomItemizedOverlay(this,
					IC_MAP_PIN_NORMAL);

			OverlayItem olditem = ((CustomItemizedOverlay) mapView
					.getOverlays().get(oldOverlay)).getItem(0);
			normalItem.addItem(olditem);
			mapView.getOverlays().set(oldOverlay, normalItem);
		}
		CustomItemizedOverlay choseItem = new CustomItemizedOverlay(this,
				IC_MAP_PIN_CHOSE);
		OverlayItem newitem = ((CustomItemizedOverlay) mapView.getOverlays()
				.get(oldOverlay)).getItem(0);
		choseItem.addItem(newitem);

		mapView.getOverlays().set(position, choseItem);
	}

	private void showCurrentLocation(GeoPoint srcGeoPoint) {
		CustomItemizedOverlay overlayItem = new CustomItemizedOverlay(this,
				IC_MAP_PIN_CURRENT);
		OverlayItem item = new OverlayItem(srcGeoPoint, "Location",
				"I'm here (" + srcGeoPoint.getLatitudeE6() / 1E6 + ","
						+ srcGeoPoint.getLongitudeE6() / 1E6 + ")");
		overlayItem.addItem(item);
		if (mapView.getOverlays().size() == 0) {
			mapView.getOverlays().add(overlayItem);
			mc.animateTo(srcGeoPoint);
		} else {
			mapView.getOverlays().set(0, overlayItem);
			if (mapView.getOverlays().size() == 1)
				mc.animateTo(srcGeoPoint);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
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
		if (adapterView.getId() == R.id.list) {
			CKIGroupActivity parent = (CKIGroupActivity) getParent();
			Intent intent = new Intent(parent, CKICheckInActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ExtraBinding.PLACE_BINDING,
					checkinResultAdapter.getItem(position));
			intent.putExtras(bundle);
			parent.startNewActivity(CKICheckInActivity.class.getSimpleName(),
					intent);
		} else if (adapterView.getId() == R.id.tv_search) {
			textView.setText(autoCompleteAdapter.getItem(position));
			showResultAutoComplete(references.get(position));
		}

	}

	public void checkIn(Place place) {
		Intent intent = new Intent(this, CKICheckInActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void moveToPoint(GeoPoint point) {
		mc.animateTo(point);
	}

	public void showResultAutoComplete(final String reference) {
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		final ProgressDialog loading = new ProgressDialog(getParent()
				.getParent());
		loading.setMessage("Loading. Please wait...");
		loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loading.setCancelable(false);
		Overlay current = mapView.getOverlays().get(0);
		mapView.getOverlays().clear();
		mapView.getOverlays().add(current);
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				GetPlaceInfomation getInfo = new GetPlaceInfomation(reference);
				try {

					getInfo.execute("");
					Place place = getInfo.get();
					OverlayItem item = new OverlayItem(place.getMapPoint(),
							place.getName(), place.getAddress() + "\n"
									+ place.getPhoneNumber());
					addOverlay(item);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				oldOverlay = -1;
				bt_showhidelist.setVisibility(View.INVISIBLE);
				layout_list.setVisibility(View.INVISIBLE);
				loading.dismiss();
			}
		});

	}

	public void showHideList(View v) {
		if (layout_list.getVisibility() == View.INVISIBLE) {
			layout_list.setVisibility(View.VISIBLE);
		} else {
			layout_list.setVisibility(View.INVISIBLE);
		}
	}

}
