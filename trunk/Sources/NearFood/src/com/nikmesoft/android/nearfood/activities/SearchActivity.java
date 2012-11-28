package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.R.id;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.facebook.android.Util;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.SearchResultAdapter;
import com.nikmesoft.android.nearfood.components.CustomItemizedOverlaySearch;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.GetPlaceHander;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;
import com.nikmesoft.android.nearfood.utils.Utilities;

@SuppressWarnings("deprecation")
@SuppressLint("ParserError")
public class SearchActivity extends MapActivity implements OnItemClickListener,
		LocationListener {

	private ListView lvSearch;
	protected SearchResultAdapter placeAdapter;
	protected ArrayList<Place> places;
	private ViewFlipper flipper;
	private int imgIndex = 1;
	private EditText ed_distance, ed_search;
	private Spinner spinner_distance;
	String str_filter = "";
	private ProgressDialog progressDialog;
	private MapView mapView;
	public MapController mc;
	private GeoPoint currentPoint;
	private LocationManager lm;
	private TextView tvNoResult;
	ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	final Handler mHandler = new Handler();
	boolean isCheckDistance = true;
	static int id_Popup, id_btn;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	@SuppressLint("ParserError")
	public void init() {
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage("Loading. Please wait...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		places = new ArrayList<Place>();
		placeAdapter = new SearchResultAdapter(this, R.layout.list_item_search,
				places);
		lvSearch.setAdapter(placeAdapter);
		lvSearch.setOnItemClickListener(this);
		tvNoResult = (TextView) findViewById(R.id.tvNoResult);
		flipper = (ViewFlipper) findViewById(R.id.details);
		ed_distance = new EditText(this);
		ed_search = (EditText) findViewById(R.id.edt_search);
		ed_search.setText(MyApplication.contentSearch);
		ed_search.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_search.setFocusableInTouchMode(true);
			}
		});
		ed_search
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (ed_search.getText().toString().trim().equals("")) {
							Toast.makeText(getApplicationContext(),
									"Please enter information to search.",
									Toast.LENGTH_SHORT).show();
							return false;
						}
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							MyApplication.contentSearch = ed_search.getText().toString().trim();
							WSLoader ws = new WSLoader();
							ws.execute();
							return true;

						}
						return false;
					}
				});

		WSLoader ws = new WSLoader();
		ws.execute();

		// Show Map
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mc = mapView.getController();
		mc.setZoom(10);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MyApplication.LOCATION_UPDATE_TIME,
				MyApplication.LOCATION_UPDATE_DISTANCE, this);
		currentPoint = new GeoPoint(16073610, 108149659);
		geoPoints.add(currentPoint);
		mHandler.post(mUpdateResults);
		CommonUtil.toastNotify(this, "Waiting for location");
		Thread t = new Thread() {
			public void run() {
				// Get the current location in start-up
				if (lm != null) {
					Location loc = lm
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (loc != null) {
						currentPoint = new GeoPoint(
								(int) (loc.getLatitude() * 1E6),
								(int) (loc.getLongitude() * 1E6));
						geoPoints.clear();
						geoPoints.add(currentPoint);
						Log.d("result", String.valueOf(loc.getLatitude())
								+ " va " + String.valueOf(loc.getLongitude()));
						mHandler.post(mUpdateResults);
					}
				}

			}
		};
		t.start();
	}

	public int getIdPopup() {
		return R.layout.popup;

	}

	public int getIdBtnView() {
		return R.id.btnPopupView;
	}

	public int getPopupName() {
		return R.id.popupName;
	}

	public int getPopupAddress() {
		return R.id.popupAddress;
	}

	public int getIdBtnCancel() {
		return R.id.btnPopupCancle;
	}

	public int getBackground() {
		return R.drawable.bg_message;
	}

	public void toActivity(Place place) {
		SearchTabGroupActivity parent = (SearchTabGroupActivity) getParent();
		Intent intent = new Intent(parent, SearchItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtra("bundlePlace", bundle);
		parent.startNewActivity(SearchItemActivity.class.getSimpleName(),
				intent);
	}

	public void addOverlay(OverlayItem item, Place place) {
		Drawable icon = getResources().getDrawable(R.drawable.ic_map_pin_chose);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(),
				icon.getIntrinsicHeight());
		CustomItemizedOverlaySearch overlayItem = new CustomItemizedOverlaySearch(
				this, icon);
		overlayItem.addItem(item, place);
		mapView.getOverlays().add(overlayItem);
		mc.animateTo(item.getPoint());
	}

	// Create runnable for updating
	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			showLocation(currentPoint);
		}
	};

	private void showLocation(GeoPoint srcGeoPoint) {
		Drawable icon = getResources().getDrawable(
				R.drawable.ic_map_pin_current);
		icon.setBounds(0, 0, icon.getIntrinsicWidth(),
				icon.getIntrinsicHeight());
		CustomItemizedOverlaySearch overlayItem = new CustomItemizedOverlaySearch(
				this, icon);
		OverlayItem item = new OverlayItem(srcGeoPoint, "Location",
				"I'm here (" + srcGeoPoint.getLatitudeE6() / 1E6 + ","
						+ srcGeoPoint.getLongitudeE6() / 1E6 + ")");
		overlayItem.addItem(item, null);
		mapView.getOverlays().clear();
		mapView.getOverlays().add(overlayItem);
		mc.animateTo(srcGeoPoint);

	}

	@SuppressLint({ "ParserError", "NewApi", "NewApi", "NewApi" })
	public void onClickListOrMap(View v) {
		AnimationFactory.flipTransition(flipper, FlipDirection.RIGHT_LEFT);
		ImageButton btnListMap = (ImageButton) v.findViewById(R.id.bt_listmap);
		if (imgIndex == 1) {
			imgIndex = 2;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_list));
		} else {
			imgIndex = 1;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_map));
		}
	}

	public void onClickLogin(View v) {

	}

	public void onClickFilter(View v) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
		View view = LayoutInflater.from(getParent()).inflate(
				R.layout.menu_filter, null);
		final CheckBox check_distance = (CheckBox) view
				.findViewById(R.id.check_distance);
		if (MyApplication.checkboxs.get(0).isChecked())
			check_distance.setChecked(true);
		check_distance
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						processCheckBoxs(check_distance, isChecked, 0);
					}
				});
		final CheckBox check_location = (CheckBox) view
				.findViewById(R.id.check_location);
		if (MyApplication.checkboxs.get(1).isChecked())
			check_location.setChecked(true);
		check_location
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						processCheckBoxs(check_location, isChecked, 1);
					}
				});
		final CheckBox check_place = (CheckBox) view
				.findViewById(R.id.check_place);
		if (MyApplication.checkboxs.get(2).isChecked())
			check_place.setChecked(true);
		check_place
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						processCheckBoxs(check_place, isChecked, 2);
					}
				});
		final CheckBox check_dishes = (CheckBox) view.findViewById(R.id.check_dishes);
		if (MyApplication.checkboxs.get(3).isChecked())
			check_dishes.setChecked(true);
		check_dishes
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						processCheckBoxs(check_dishes, isChecked, 3);
					}
				});
		ed_distance = (EditText) view.findViewById(R.id.ed_distance);
		ed_distance.setText(String.valueOf(MyApplication.distance));
		spinner_distance = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getParent(), R.array.distance_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_distance.setAdapter(adapter);
		spinner_distance.setSelection(MyApplication.distanceByKms);
		alert.setTitle("Serach Filter");
		alert.setView(view);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MyApplication.distance = Double.parseDouble(ed_distance.getText().toString()
						.trim());
				MyApplication.distanceByKms = spinner_distance.getSelectedItemPosition();
			}
		});
		AlertDialog dialog = alert.create();
		dialog.show();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Place place = places.get(arg2);
		SearchTabGroupActivity parent = (SearchTabGroupActivity) getParent();
		Intent intent = new Intent(parent, SearchItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtra("bundlePlace", bundle);
		parent.startNewActivity(SearchItemActivity.class.getSimpleName(),
				intent);
	}

	Runnable runTimeout = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isCheckDistance = true;
		}
	};

	public void processCheckBoxs(CheckBox checkbox, boolean isChecked,
			int position) {
		int count = 0;
		MyApplication.checkboxs.get(position).setChecked(isChecked);
		for (int i = 0; i < MyApplication.checkboxs.size(); i++)
			if (MyApplication.checkboxs.get(i).isChecked())
				count++;
		if (count < 1) {
			checkbox.setChecked(true);
			MyApplication.checkboxs.get(position).setChecked(true);
			if (isCheckDistance) {
				isCheckDistance = false;
				Toast.makeText(getApplicationContext(),
						"You must to select at least a item.",
						Toast.LENGTH_SHORT).show();
				Thread d = new Thread(runTimeout);
				d.start();
			}
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void actionSearch(View v) {
		if (ed_search.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(),
					"Please enter information to search.", Toast.LENGTH_SHORT)
					.show();
		} else {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			MyApplication.contentSearch = ed_search.getText().toString().trim();
			WSLoader ws = new WSLoader();
			ws.execute();
		}
	}

	private Object xmlParser(String strXml) {
		byte xmlBytes[] = strXml.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				xmlBytes);
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxPF.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();
			GetPlaceHander handler = new GetPlaceHander();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(byteArrayInputStream));
			return handler.getResult();

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String requests() {

		return null;
	}

	private class WSLoader extends AsyncTask<String, Integer, Object> {
		@Override
		protected Object doInBackground(String... params) {
			double distance_ = MyApplication.distance;
			if (!ed_distance.getText().toString().trim().equals(""))
				Double.parseDouble(ed_distance.getText().toString().trim());
			if (MyApplication.distanceByKms == 1) {
				distance_ *= 1.60934;
			}
			String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<getPlaces soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ "<GetPlacesRequest xsi:type=\"sfo:GetPlacesRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ "<!--You may enter the following 9 items in any order-->"
					+ "<latitude xsi:type=\"xsd:double\">108</latitude>"
					+ "<longitude xsi:type=\"xsd:double\">16</longitude>"
					+ "<filter_distance xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(0).isChecked())
					+ "</filter_distance>"
					+ "<distance xsi:type=\"xsd:double\">"
					+ String.valueOf(distance_)
					+ "</distance>"
					+ "<filter_address xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(1).isChecked())
					+ "</filter_address>"
					+ "<filter_name xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(2).isChecked())
					+ "</filter_name>"
					+ "<filter_dishes xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(3).isChecked())
					+ "</filter_dishes>"
					+ "<key xsi:type=\"xsd:string\">"
					+ ed_search.getText().toString().trim()
					+ "</key>"
					+ "<page xsi:type=\"xsd:int\">"
					+ Integer.parseInt("1")
					+ "</page>"
					+ "</GetPlacesRequest>"
					+ "</getPlaces>"
					+ "</soapenv:Body>" + "</soapenv:Envelope>";
			String soapAction = "http://nikmesoft.com/apis/SFoodServices/index.php/getCheckIns";
			return xmlParser(Utilities.callWS(request, soapAction));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result==null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				builder.show();
			} else {
				if (result.getClass().equals(ErrorCode.class)) {
				} else {
					placeAdapter.clear();
					// get current location
					Overlay tempOverlay = mapView.getOverlays().get(0);
					mapView.getOverlays().clear();
					mapView.getOverlays().add(tempOverlay);
					GeoPoint tempGeopoint = geoPoints.get(0);
					geoPoints.clear();
					geoPoints.add(tempGeopoint);
					if (((ArrayList<Place>) result).size() > 0) {
						lvSearch.setVisibility(View.VISIBLE);
						tvNoResult.setVisibility(View.GONE);
						for (Place place : ((ArrayList<Place>) result)) {
							placeAdapter.add(place);
							geoPoints.add(place.getMapPoint());
							OverlayItem item = new OverlayItem(
									place.getMapPoint(), place.getName(),
									place.getAddress());
							addOverlay(item, place);
						}
						placeAdapter.notifyDataSetChanged();
					} else {
						lvSearch.setVisibility(View.GONE);
						tvNoResult.setVisibility(View.VISIBLE);
					}
					if (mapView.getOverlays().size() > 0) {
						mc.animateTo(geoPoints.get(0));
					}
				}
				
			}
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
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

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
