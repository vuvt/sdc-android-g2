package com.nikmesoft.android.nearfood.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

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
import com.nikmesoft.android.nearfood.components.GetPlacesAutoComplete;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.Utilities;

public class CKIMainActivity extends BaseMapsActivity implements
		LocationListener, AdapterView.OnItemClickListener {

	private Drawable IC_MAP_PIN_NORMAL;
	private Drawable IC_MAP_PIN_CHOSE;
	private Drawable IC_MAP_PIN_CURRENT;
	private ListView list;
	private AutoCompleteTextView textView;
	private CheckInResultAdapter checkinResultAdapter;
	MapView mapView;
	private MapController mc;
	private static GeoPoint currentPoint;
	private LocationManager lm;
	private final Handler mHandler = new Handler();
	private ArrayAdapter<String> autoCompleteAdapter;
	public int oldOverlay = -1;

	private Button bt_showhidelist;
	private LinearLayout layout_list;
	private GetPlacesAutoComplete getplaceAutoComplete = null;
	private SearchHandle handle = new SearchHandle(this);
	private ProgressDialog loading;
	private TextView txt_no_results;
	Thread search,checktimeout;

	// private TextView txtNoResult;

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
		// txtNoResult=(TextView)findViewById(R.id.txt_noresults);
		// Add data to list
		list = (ListView) findViewById(R.id.list);
		checkinResultAdapter = new CheckInResultAdapter(this,
				R.layout.activity_checkin_list_item, new ArrayList<Place>());
		checkinResultAdapter.setNotifyOnChange(true);
		list.setAdapter(checkinResultAdapter);
		list.setOnItemClickListener(this);
		bt_showhidelist = (Button) findViewById(R.id.bt_showhidelist);
		layout_list = (LinearLayout) findViewById(R.id.layout_list);

		// Map define component

		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		// mc.setCenter(new GeoPoint(108149794, 16073651));
		mc.setZoom(16);
		currentPoint = new GeoPoint(1607361, 108149659);
		OverlayItem item = new OverlayItem(currentPoint, "Current address",
				"16.07361,108.149659");

		CustomItemizedOverlay overlayItem = new CustomItemizedOverlay(this,
				IC_MAP_PIN_CURRENT);
		overlayItem.addItem(item);
		mapView.getOverlays().add(overlayItem);
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
		autoCompleteAdapter.setNotifyOnChange(true);
		textView.setAdapter(autoCompleteAdapter);
		textView.setOnItemClickListener(this);
		textView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (getplaceAutoComplete != null
						&& !getplaceAutoComplete.isCancelled()) {
					getplaceAutoComplete.cancel(true);
				}
				getplaceAutoComplete = new GetPlacesAutoComplete(
						autoCompleteAdapter, currentPoint);
				getplaceAutoComplete.execute(textView.getText().toString());
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

		});
		loading = new ProgressDialog(getParent());
		loading.setMessage("Loading. Please wait...");
		loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		loading.setCancelable(false);
		txt_no_results = (TextView) findViewById(R.id.txt_no_results);
		

	}

	public void actionSearch(View v) {
		performSearch();

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
		// if (mapView.getOverlays().size() == 0) {
		// mapView.getOverlays().add(overlayItem);
		// mc.animateTo(srcGeoPoint);
		// } else {
		mapView.getOverlays().set(0, overlayItem);
		if (mapView.getOverlays().size() == 1)
			mc.animateTo(srcGeoPoint);
		// }

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

	public void showHideList(View v) {
		if (layout_list.getVisibility() == View.INVISIBLE) {
			layout_list.setVisibility(View.VISIBLE);
		} else {
			layout_list.setVisibility(View.INVISIBLE);
		}
	}
	private Runnable countTimeOut=new Runnable() {
		
		@Override
		public void run() {
			for(int i=0;i<15;i++){
				SystemClock.sleep(1000);
			}
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putSerializable("ERROR", CKIMainActivity.this.getResources().getString(R.string.title_connection_timeout));
			msg.setData(b);
			handle.sendMessage(msg);
		}
	};

	private Runnable loader = new Runnable() {

		public void run() {
			MyArrayList places = new MyArrayList();
			Message msg = new Message();
			Bundle b = new Bundle();
			try {
				String text_search = URLEncoder.encode(textView.getText()
						.toString().trim(), "UTF-8");
				URL googlePlaces = new URL(
						"https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword="
								+ text_search
								+ "&location="
								+ currentPoint.getLatitudeE6()
								/ 1000000.0
								+ ","
								+ currentPoint.getLongitudeE6()
								/ 1000000.0
								+ "&radius="
								+ MyApplication.SEARCH_RADIUS
								+ "&type=establisment&sensor=true&key=AIzaSyC1VTuBKDDynoLGUZqS9141VJ0KIF1wXss");

				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				Log.d("Respone", sb.toString());
				// b.putString("RESPONE", sb.toString());
				JSONObject search = new JSONObject(sb.toString());
				JSONArray jresults = new JSONArray(search.getString("results"));

				for (int i = 0; i < jresults.length(); i++) {
					JSONObject jo = (JSONObject) jresults.get(i);
					JSONObject point = jo.getJSONObject("geometry")
							.getJSONObject("location");
					Place place = new Place();
					place.setNamePlace(jo.getString("name"))
							.setAddress(jo.getString("vicinity"))
							.setReferenceKey(jo.getString("id"))
							.setMapPoint(
									new GeoPoint(
											(int) (point.getDouble("lat") * 1E6),
											(int) (point.getDouble("lng") * 1E6)));
					try {
						place.setImagePath(jo.getString("icon"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					places.add(place);

				}

			} catch (MalformedURLException e) {
				b.putString("ERROR", e.getMessage());
			} catch (IOException e) {
				b.putString(
						"ERROR",
						getResources().getString(
								R.string.error_network_connection));
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				b.putString("ERROR", e.getMessage());
			}

			b.putSerializable("RESULT", places);
			msg.setData(b);
			handle.sendMessage(msg);
		}
	};

	private void performSearch() {

		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

		loading.show();
		search = new Thread(loader);
		
		checktimeout=new Thread(countTimeOut);
		search.start();
		checktimeout.start();
	}

	static class SearchHandle extends Handler {
		CKIMainActivity activity;

		SearchHandle(CKIMainActivity activity) {
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {

			// activity.txtNoResult.setText(
			// msg.getData().getString("RESPONE"));
			String error = msg.getData().getString("ERROR");
			if (error != null) {
				if(activity.checktimeout!=null&&!activity.checktimeout.isInterrupted())
					activity.checktimeout.interrupt();
				CommonUtil.dialogNotify(activity.getParent(), error);
			}

			else {
				if(activity.checktimeout!=null&&!activity.checktimeout.isInterrupted())
					activity.checktimeout.interrupt();
				MyArrayList results = (MyArrayList) msg.getData()
						.getSerializable("RESULT");
				activity.checkinResultAdapter.clear();
				activity.checkinResultAdapter.addAll(results);
				activity.checkinResultAdapter.notifyDataSetChanged();
				try {
					Overlay temp = activity.mapView.getOverlays().get(0);
					activity.mapView.getOverlays().clear();
					activity.mapView.getOverlays().add(temp);
				} catch (Exception e) {
					activity.mapView.getOverlays().clear();
				}

				for (int i = 0; i < results.size(); i++) {
					OverlayItem item = new OverlayItem(results.get(i)
							.getMapPoint(), results.get(i).getName(), results
							.get(i).getAddress());
					activity.addOverlay(item);
				}
				if (results.size() > 0)
					activity.mc.animateTo(results.get(0).getMapPoint());
				if (results.size() == 0) {
					activity.list.setVisibility(View.GONE);
					activity.txt_no_results.setVisibility(View.VISIBLE);
				} else {
					activity.list.setVisibility(View.VISIBLE);
					activity.txt_no_results.setVisibility(View.GONE);
				}
				activity.layout_list.setVisibility(View.VISIBLE);
				activity.bt_showhidelist.setVisibility(View.VISIBLE);
			}
			activity.loading.dismiss();
			activity.oldOverlay = -1;
			// String url;

		}

	}

	@SuppressWarnings("serial")
	public class MyArrayList extends ArrayList<Place> implements Serializable {
		public MyArrayList() {
			super();
		}

	}

}
