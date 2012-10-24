package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.CheckIn;

public class CheckInAdapter extends ArrayAdapter<CheckIn>{
		private int resourceID;
		
		public CheckInAdapter(Context context, int resourceID, List<CheckIn> objects) {
			super(context, resourceID, objects);
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
			CheckIn checkin=getItem(position);
			
			TextView tv_username= (TextView) row.findViewById(R.id.tv_username);
			tv_username.setText(checkin.getUser().getFullName());
			
			TextView tv_email= (TextView) row.findViewById(R.id.tv_email);
			tv_email.setText(checkin.getUser().getEmail());
			
			TextView tv_description= (TextView) row.findViewById(R.id.tv_checkin_description);
			tv_description.setText(checkin.getDescription());
			
			ImageView img_checkin=(ImageView)row.findViewById(R.id.img_checkin);
			img_checkin.setImageDrawable(row.getContext().getResources().getDrawable(R.drawable.test_checkin));
			
			return row;
		}
}
