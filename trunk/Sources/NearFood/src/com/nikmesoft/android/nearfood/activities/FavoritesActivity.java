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

import com.facebook.PlacePickerFragment;
import com.google.android.maps.MapActivity;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.FavoriteResultAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchResultAdapter;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.GetPlaceHander;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.Utilities;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends MapActivity implements OnItemClickListener {

	private ListView lvSearch;
	protected FavoriteResultAdapter placeAdapter;
	protected ArrayList<Place> places;
	private ViewFlipper flipper;
	private int imgIndex = 1, distanceByKms = 0;
	private EditText ed_distance, ed_search;
	ArrayList<CheckBox> checkboxs;
	private ProgressDialog progressDialog;
	private double distance;
	private Spinner spinner_distance;
	String str_filter = "";
	
	private final static int REQUEST_LOGIN = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_favorites);
		init();
		//checklogin();
	}
	
	private void init() {
		distance = 8235;
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage("Loading. Please wait...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		checklogin();
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		places = new ArrayList<Place>();
		placeAdapter = new FavoriteResultAdapter(this, R.layout.list_item_search,places);
		lvSearch.setAdapter(placeAdapter);
		lvSearch.setOnItemClickListener(this);
		flipper = (ViewFlipper) findViewById(R.id.details);
		checkboxs = new ArrayList<CheckBox>();
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
							placeAdapter.clear();
							((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);
							//MyApplication.contentSearch = ed_search.getText().toString().trim();
							WSLoader ws = new WSLoader();
							ws.execute();
							for (Place plac : places) {
								if(plac.getName().equals(ed_search.getText())){
									placeAdapter.add(plac);
								}
							}
							return true;

						}
						return false;
					}
				});
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				placeAdapter.clear();
				placeAdapter.notifyDataSetChanged();
				//page = 1 ;
				progressDialog.setMessage("Reloading. Please wait...");
				//WSLoader ws = new WSLoader();
				//ws.execute();
				Log.d("Favorites check test", "Test"+ placeAdapter);
			}   
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.nikmesoft.android.nearfood.activities.NOT_LOGIN_BROADCAST");
			registerReceiver(receiver, filter);
	}
	public void actionSearch(View v) {
		if (ed_search.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(),
					"Please enter information to search.", Toast.LENGTH_SHORT)
					.show();
		} else {
			placeAdapter.clear();
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			//MyApplication.contentSearch = ed_search.getText().toString().trim();
			WSLoader ws = new WSLoader();
			ws.execute();
			for (Place plac : places) {
				if(plac.getName().equals(ed_search.getText())){
					placeAdapter.add(plac);
				}
			}
		}
	}
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

	/*public void onClickFilter(View v) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
		View view = LayoutInflater.from(this).inflate(
				R.layout.menu_filter, null);
		Log.d("Viewwwwwwww", String.valueOf(view.getId()));
		final CheckBox check_distance = (CheckBox) view
				.findViewById(R.id.check_distance);
		final CheckBox check_location = (CheckBox) view
				.findViewById(R.id.check_location);
		final CheckBox check_place = (CheckBox) view
				.findViewById(R.id.check_place);
		final CheckBox check_dishes = (CheckBox) view
				.findViewById(R.id.check_dishes);
		ed_distance = (EditText) view.findViewById(R.id.ed_distance);
		spinner_distance = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.distance_array,
				android.R.layout.simple_spinner_item);
		spinner_distance.setAdapter(adapter);
		alert.setTitle("Serach Filter");
		alert.setView(view);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				checkboxs.clear();
				str_filter = "";
				if (check_distance.isChecked()) {
					checkboxs.add(check_distance);
					str_filter += ed_distance.getText().toString()
							+ (String) spinner_distance.getSelectedItem()
									.toString() + " of "
							+ check_distance.getText().toString() + " - ";
				}
				if (check_location.isChecked()) {
					checkboxs.add(check_location);
					str_filter += check_location.getText().toString() + " - ";
				}
				if (check_place.isChecked()) {
					checkboxs.add(check_place);
					str_filter += check_place.getText().toString() + " - ";
				}
				if (check_dishes.isChecked()) {
					checkboxs.add(check_dishes);
					str_filter += check_dishes.getText().toString();
				}
				Toast.makeText(getApplicationContext(), str_filter,
						Toast.LENGTH_SHORT).show();

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog dialog = alert.create();
		dialog.show();
	}
*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Place place = places.get(arg2);
		FavoritesTabGroupActivity parent = (FavoritesTabGroupActivity) getParent();
		Intent intent = new Intent(parent, FavoritesItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtra("bundlePlace", bundle);
		parent.startNewActivity(FavoritesItemActivity.class.getSimpleName(),
				intent);
		
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
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
	@TargetApi(11)
	private class WSLoader extends AsyncTask<String, Integer, Object> {

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onCancelled(Object result) {
			super.onCancelled(result);
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
						finish();
					}
				});
				builder.show();
			} else {
				if (result.getClass().equals(ErrorCode.class)) {
				} else {
					placeAdapter.clear();
					if (((ArrayList<Place>) result).size() > 0) {
						lvSearch.setVisibility(View.VISIBLE);
						for (Place place : ((ArrayList<Place>) result)) {
							placeAdapter.add(place);
						}
						lvSearch.setAdapter(placeAdapter);
						placeAdapter.notifyDataSetChanged();
					} else {
						lvSearch.setVisibility(View.GONE);
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

		@Override
		protected Object doInBackground(String... arg0) {			
		String request =
				"<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
				   "<soapenv:Header/>"+
				   "<soapenv:Body>"+
				     " <getFavorites soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"+
				      " <GetFavoritesRequest xsi:type=\"sfo:GetFavoritesRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"+
				       "<!--You may enter the following 5 items in any order-->"+
				        "<latitude xsi:type=\"xsd:double\">108.208611</latitude>"+
				         "<longitude xsi:type=\"xsd:double\">16.071053</longitude>"+
				          "<id_user xsi:type=\"xsd:int\">"+46+"</id_user>"+
				           " <key xsi:type=\"xsd:string\"></key>"+
				         "<page xsi:type=\"xsd:int\">1</page>"+
				       "</GetFavoritesRequest>"+
				      "</getFavorites>"+
				   "</soapenv:Body> "+
			"</soapenv:Envelope>";
		String soapAction = " http://nikmesoft.com/apis/SFoodServices/index.php/getFavorites";
		return xmlParser(Utilities.callWS(request, soapAction));
		}
	}
	private void checklogin(){
		if(MyApplication.USER_CURRENT==null){
			AlertDialog.Builder builder = new AlertDialog.Builder(getParent());		
	        builder.setMessage("You need to be logged in to use this function"+"\n Would you like to login !")
	               .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id){
	                		Intent intent = new Intent();
	                		intent.setClass(getParent(), LoginActivity.class);
	                		getParent().startActivityForResult(intent, REQUEST_LOGIN);
	                		BroadcastReceiver receiver = new BroadcastReceiver() {
	                			@Override
	                			public void onReceive(Context arg0, Intent arg1) {
	                				//placeAdapter.clear();
	                				//placeAdapter.notifyDataSetChanged();
	                				//page = 1 ;
	                				//progressDialog.setMessage("Reloading. Please wait...");
	                				WSLoader ws = new WSLoader();
	                				ws.execute();
	                			}   
	                			};
	                			IntentFilter filter = new IntentFilter();
	                			filter.addAction("com.nikmesoft.android.nearfood.activities.LATER_LOGIN_BROADCAST");
	                			registerReceiver(receiver, filter);
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                   }
	               });
	        builder.show();
	        
		}
		else{
			//Log.d("daddaaaaaaaaaaaaaaaaaaaaaaaaaaaad", "dddddddddddddddddddddddddddddddddddddddddddddddda");
		WSLoader ws = new WSLoader();
		ws.execute();
		}
	}
	
/*	protected void onGroupActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_LOGIN){
			if(resultCode==RESULT_OK){
				checklogin();
				Log.d("adadadaddaddadad", "dada");
			}
			
		}
		
	}*/
	

	
}
