package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Restaurant;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
	private int resourceID;

	public RestaurantAdapter(Context context, int resourceID, List<Restaurant> objects) {
		super(context, resourceID, objects);
		this.resourceID = resourceID;
	}

	@SuppressLint({ "ParserError", "ParserError", "NewApi" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Restaurant restaurant = getItem(position);
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater;
			inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.resourceID, null);
		}
		
		TextView nameRestaurant = (TextView) row.findViewById(R.id.namePlace_item_Search);
		TextView addressRestaurant = (TextView) row.findViewById(R.id.address_item_Search);
		TextView distanceRestaurant = (TextView) row.findViewById(R.id.distance_item_Search);
		TextView numberLike = (TextView) row.findViewById(R.id.number_like_Search);
		
		nameRestaurant.setText(restaurant.getNameRestaurant().toString());
		addressRestaurant.setText(restaurant.getAddress().toString());
		distanceRestaurant.setText("About 2.5 Miles");
		numberLike.setText(restaurant.getNumberLike());
		
		return row;
	}
}
