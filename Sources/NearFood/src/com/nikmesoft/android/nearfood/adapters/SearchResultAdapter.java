package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Place;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		Place place = getItem(position);
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
		
		nameplace.setText(place.getName().toString());
		addressplace.setText(place.getAddress().toString());
		distanceplace.setText("About 2.5 Miles");
		numberLike.setText(String.valueOf(place.getLikedCount()));
		
		return row;
	}
}
