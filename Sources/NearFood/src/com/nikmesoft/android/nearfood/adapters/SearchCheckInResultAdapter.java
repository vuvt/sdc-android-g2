package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.activities.CKIMainActivity;
import com.nikmesoft.android.nearfood.models.CheckIn;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchCheckInResultAdapter extends ArrayAdapter<CheckIn> {
	private int resourceID;
	private CKIMainActivity context;

	public SearchCheckInResultAdapter(Context context, int resourceID,
			List<CheckIn> objects) {
		super(context, resourceID, objects);
		this.resourceID = resourceID;
		this.context = (CKIMainActivity) context;
	}

	@SuppressLint({ "ParserError", "ParserError", "NewApi" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CheckIn check_in = getItem(position);
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater;
			inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.resourceID, null);
		}

		TextView fullName = (TextView) row
				.findViewById(R.id.fullname_item_checkin_search);
		TextView description = (TextView) row
				.findViewById(R.id.description_item_checkin_search);
		TextView time = (TextView) row
				.findViewById(R.id.time_item_checkin_search);

		// fullName.setText(check_in.getIdUser().toString());
		fullName.setText("Full Name");
		description.setText(check_in.getPlace().getDescription().toString());
		time.setText(check_in.getTimeCheck().toString());
		ImageView img_map = (ImageView) row.findViewById(R.id.img_mapview);
		img_map.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				context.setChosenOverlay(position + 1);

			}
		});
		return row;
	}
}
