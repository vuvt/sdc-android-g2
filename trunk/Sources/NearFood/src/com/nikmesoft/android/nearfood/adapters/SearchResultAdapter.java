package com.nikmesoft.android.nearfood.adapters;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SearchResultAdapter extends ArrayAdapter<Place> {
	private int resourceID;
	public SearchResultAdapter(Context context, int resourceID, List<Place> objects) {
		super(context, resourceID, objects);
		this.resourceID = resourceID;
	}

	@SuppressLint({ "ParserError", "ParserError", "NewApi" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Place place = getItem(position);
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater;
			inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.resourceID, null);
		}
		
		TextView nameplace = (TextView) row.findViewById(R.id.namePlace_item_Search);
		TextView addressplace = (TextView) row.findViewById(R.id.address_item_Search);
		TextView distanceplace = (TextView) row.findViewById(R.id.distance_item_Search);
		TextView numberLike = (TextView) row.findViewById(R.id.number_like_Search);
		ImageView img = (ImageView) row.findViewById(R.id.img_item_Search);
		if(!place.getImagePath().equals("")){
		ImageLoader imageLoader = new ImageLoader();
		ArrayList<Object> object = new ArrayList<Object>();
		object.add(place.getImagePath());
		object.add(img);
		imageLoader.execute(object);
		}
		nameplace.setText(place.getName().toString());
		addressplace.setText(place.getAddress().toString());
		distanceplace.setText(place.getDistance());
		numberLike.setText(String.valueOf(place.getLikedCount()));
		/*if(place.getImagePath()!= null){
		new Thread(new Runnable() {

			public void run() {

				try {
					Drawable drawable = Utilities.LoadImageFromWebOperations(
							place.getImagePath(), place.getImagePath());
					img.setImageDrawable(drawable);
				} catch (Exception e) {
				}

			}
		}).start();
		}*/
		return row;
	}
	private class ImageLoader extends AsyncTask<ArrayList<Object>, Drawable, Drawable> {
		protected ImageView img;
		@Override
		protected Drawable doInBackground(ArrayList<Object>... params) {
			// TODO Auto-generated method stub
			try {
				img = (ImageView)params[0].get(1);
				return Utilities.LoadImageFromWebOperations(
						(String)params[0].get(0), (String)params[0].get(0));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// TODO Auto-generated method stub
			img.setImageDrawable(result);
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Drawable... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			img.setImageDrawable(values[0]);
		}
		
	}
}
