package com.nikmesoft.android.nearfood.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.nikmesoft.android.nearfood.models.Place;

import android.os.AsyncTask;
import android.util.Log;

public class GetPlaceInfomation extends
		AsyncTask<String, Void, ArrayList<String>> {

	String reference;
	public Place place;
	public boolean ended;

	public GetPlaceInfomation(String reference) {
		this.reference = reference;
		this.place = null;
		ended=false;
	}

	@SuppressWarnings({ "rawtypes" })
	protected ArrayList doInBackground(String... args) {

		Log.d("gottaGo", "doInBackground");

		ArrayList<String[]> predictionsArr = new ArrayList<String[]>();

		try {

			URL googlePlaces = new URL(
					"https://maps.googleapis.com/maps/api/place/details/xml?reference="
							+ reference
							+ "&sensor=true&key=AIzaSyC1VTuBKDDynoLGUZqS9141VJ0KIF1wXss");
			// https://maps.googleapis.com/maps/api/place/search/json?key=0sJSo9PVeQmdLNTNqGBRmhz1B-5KovBnHbEXQoA&location=37.994682,-87.6045923&radius=500&sensor=false&types-geocode");
			// "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+
			// URLEncoder.encode(arg0[0], "UTF-8")
			// +"&types=geocode&language=fr&sensor=true&key=" +
			// 0sJSo9PVeQmdLNTNqGBRmhz1B-5KovBnHbEXQoA);
			URLConnection tc = googlePlaces.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			Log.d("Content", sb.toString());
			JSONObject placeDetail = new JSONObject(sb.toString());
			JSONObject result = placeDetail.getJSONObject("result");
			JSONObject point = result.getJSONObject("geometry").getJSONObject(
					"location");
			place = new Place();
			place.setNamePlace(result.getString("name"))
					.setAddress(result.getString("formatted_address"))
					.setReferenceKey(reference)
					.setMapPoint(
							new GeoPoint((int) (point.getDouble("lat") * 1E6),
									(int) (point.getDouble("lng") * 1E6)));

			try {
				place.setPhoneNumber(result.getString("formatted_phone"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				place.setImagePath(result.getString("icon"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ended=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ended=true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ended=true;
		}

		return predictionsArr;

	}

	// then our post

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected void onPostExecute(ArrayList result) {
		ended=true;
	}

}
