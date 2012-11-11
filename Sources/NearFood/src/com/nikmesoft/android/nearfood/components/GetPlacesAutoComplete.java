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
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.activities.CKIMainActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;


@SuppressLint({ "NewApi", "NewApi" })
public class GetPlacesAutoComplete extends
		AsyncTask<String, Void, ArrayList<String>> {


	ArrayAdapter<String> adapter;
	ArrayList<String> places = new ArrayList<String>();
	GeoPoint currentPoint;

	public GetPlacesAutoComplete(ArrayAdapter<String> adapter,GeoPoint currentPoint) {
		this.adapter = adapter;
		this.currentPoint=currentPoint;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	protected ArrayList doInBackground(String... args) {

		Log.d("gottaGo", "doInBackground");

		try {

			URL googlePlaces = new URL(
					// URLEncoder.encode(url,"UTF-8");
					"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
							+ URLEncoder.encode(args[0].toString(),
									"UTF-8")
							+ "&location=" + currentPoint.getLatitudeE6()/1000000.0 + ","+currentPoint.getLongitudeE6()/1000000.0
							+ "&radius=" + MyApplication.SEARCH_RADIUS
							+ "&sensor=true&key=AIzaSyC1VTuBKDDynoLGUZqS9141VJ0KIF1wXss");

			URLConnection tc = googlePlaces.openConnection();
			Log.d("GottaGo", URLEncoder.encode(args[0]));
			BufferedReader in = new BufferedReader(new InputStreamReader(
					tc.getInputStream()));

			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			// Log.d("Content", sb.toString());
			JSONObject predictions = new JSONObject(sb.toString());
			// Log.d("Prediction", predictions.toString());
			JSONArray ja = new JSONArray(predictions.getString("predictions"));

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				{
					places.add(jo.getString("description"));

				}

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

	// then our post

	@SuppressWarnings({  "rawtypes" })
	@Override
	protected void onPostExecute(ArrayList result) {

		Log.d("YourApp", "onPostExecute : " + result.size());
		// update the adapter

		adapter.clear();
		adapter.addAll(places);
		Log.d("YourApp",
				"onPostExecute : autoCompleteAdapter " + adapter.getCount());

	}
	/*public void onCancle(){
		
	}*/
	
}
