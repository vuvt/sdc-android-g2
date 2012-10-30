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
import com.nikmesoft.android.nearfood.adapters.CheckInResultAdapter;
import com.nikmesoft.android.nearfood.models.Place;

import android.os.AsyncTask;
import android.util.Log;

public class GetPlaces extends AsyncTask<String, Void, ArrayList<Place>> {

	String input;
	boolean isAutoComplete;
	CheckInResultAdapter adapter;
	ArrayList<Place> places;


	protected ArrayList<Place> doInBackground(String... args) {
		// = ProgressDialog.show(MyApplication.getContext(), "Loading...",
		// "Please wait!", true, false);

		Log.d("gottaGo", "doInBackground");
		places = new ArrayList<Place>();

		try {

			URL googlePlaces = new URL(
					"https://maps.googleapis.com/maps/api/place/textsearch/json?query="
							+ URLEncoder.encode(args[0], "UTF-8")
							+ "&sensor=true&key=AIzaSyC1VTuBKDDynoLGUZqS9141VJ0KIF1wXss");
			URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			JSONObject search = new JSONObject(sb.toString());
			JSONArray jresults = new JSONArray(search.getString("results"));

			for (int i = 0; i < jresults.length(); i++) {
				JSONObject jo = (JSONObject) jresults.get(i);
				JSONObject point = jo.getJSONObject("geometry").getJSONObject(
						"location");
				Place place = new Place();
				place.setNamePlace(jo.getString("name"))
						.setAddress(jo.getString("formatted_address"))
						.setReferenceKey(jo.getString("reference"))
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return places;

	}

}
