package com.nikmesoft.android.nearfood.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CheckInResultAdapter;
import com.nikmesoft.android.nearfood.models.Place;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

public class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

	AutoCompleteTextView textView;
	CheckInResultAdapter adapter;
	MapView mapView;

	public GetPlaces(AutoCompleteTextView textView,
			CheckInResultAdapter adapter, MapView mapView) {
		this.textView = textView;
		this.adapter = adapter;
		this.mapView = mapView;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	protected ArrayList doInBackground(String... args) {

		Log.d("gottaGo", "doInBackground");

		ArrayList<Place> predictionsArr = new ArrayList<Place>();

		try {

			URL googlePlaces = new URL(
					"https://maps.googleapis.com/maps/api/place/nearbysearch/json?input="
							+ URLEncoder.encode(textView.toString(), "UTF-8")
							+ "&sensor=true&key=AIzaSyC1VTuBKDDynoLGUZqS9141VJ0KIF1wXss");
			URLConnection tc = googlePlaces.openConnection();
			Log.d("GottaGo", URLEncoder.encode(textView.getText().toString()));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			JSONObject search = new JSONObject(sb.toString());
			JSONArray  jresults = new JSONArray(search.getString("results"));

			for (int i = 0; i < jresults.length(); i++) {
				JSONObject jo = (JSONObject) jresults.get(i);
				JSONObject point = jo.getJSONObject("geometry").getJSONObject(
						"location");
				Place place = new Place();
				place.setNamePlace(jo.getString("name"))
				.setAddress(jo.getString("formatted_address"))
				.setReferenceKey(jo.getString("reference"))
				.setMapPoint(
						new GeoPoint((int) (point.getDouble("lat") * 1E6),
								(int) (point.getDouble("lng") * 1E6)));
				try {
					place.setImagePath(jo.getString("icon"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				predictionsArr.add(place);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return predictionsArr;

	}

	// then our post

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onPostExecute(ArrayList result) {

		Log.d("YourApp", "onPostExecute : " + result.size());
		// update the adapter
		adapter = new CheckInResultAdapter(textView.getContext(),
				R.layout.activity_checkin_list_item, (ArrayList<Place>) result,
				mapView);
		adapter.setNotifyOnChange(true);
		// attach the adapter to textview
		textView.setAdapter(adapter);

		Log.d("YourApp",
				"onPostExecute : autoCompleteAdapter" + adapter.getCount());

	}

}
