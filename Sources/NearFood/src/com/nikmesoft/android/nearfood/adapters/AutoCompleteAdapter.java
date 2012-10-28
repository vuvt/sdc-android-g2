package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Place;

public class AutoCompleteAdapter extends ArrayAdapter<Place>{
	private int resourceID;
	
	public AutoCompleteAdapter(Context context, int resourceID) {
		super(context, resourceID);
		this.resourceID = resourceID;

	}
	public AutoCompleteAdapter(Context context, int resourceID,List<Place> list) {
		super(context, resourceID,list);
		this.resourceID = resourceID;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater;
			inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.resourceID, null);
		}
		TextView placeName= (TextView) row.findViewById(R.id.place_name);
		placeName.setText(getItem(position).getName());
		return row;
	}
	
}
