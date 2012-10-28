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

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.AutoCompleteAdapter;
import com.nikmesoft.android.nearfood.models.Place;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

public class GetPlacesAutoComplete extends
		AsyncTask<String, Void, ArrayList<String>> {

	AutoCompleteTextView textView;
	AutoCompleteAdapter adapter;
	ArrayList<Place> places = new ArrayList<Place>();

	public GetPlacesAutoComplete(AutoCompleteTextView textView,
			AutoCompleteAdapter adapter) {
		this.textView = textView;
		this.adapter = adapter;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	protected ArrayList doInBackground(String... args) {

		Log.d("gottaGo", "doInBackground");

		

		try {

			URL googlePlaces = new URL(
					// URLEncoder.encode(url,"UTF-8");
					"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
							+ URLEncoder.encode(textView.getText().toString(),
									"UTF-8")
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
			//Log.d("Content", sb.toString());
			JSONObject predictions = new JSONObject(sb.toString());
			//Log.d("Prediction", predictions.toString());
			JSONArray ja = new JSONArray(predictions.getString("predictions"));

			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = (JSONObject) ja.get(i);
				{
					places.add(new Place(jo.getString("description"), jo
							.getString("reference")));

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onPostExecute(ArrayList result) {

		Log.d("YourApp", "onPostExecute : " + result.size());
		// update the adapter
		adapter = new AutoCompleteAdapter(textView.getContext(),
				R.layout.activity_checkin_auto_complete_list_item,
				places);
		// attach the adapter to textview
		textView.setAdapter(adapter);

		Log.d("YourApp",
				"onPostExecute : autoCompleteAdapter " + adapter.getCount());

	}
}
