package com.nikmesoft.android.nearfood.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.CheckIn;

public class CheckInAdapter extends ArrayAdapter<CheckIn> {
	private int resourceID;
	private Context context;

	public CheckInAdapter(Context context, int resourceID, List<CheckIn> objects) {
		super(context, resourceID, objects);
		this.resourceID = resourceID;
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater;
			inflater = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(this.resourceID, null);
		}
		CheckIn checkin = getItem(position);

		TextView tv_username = (TextView) row.findViewById(R.id.tv_username);
		tv_username.setText(checkin.getUser().getFullName());

		TextView tv_email = (TextView) row.findViewById(R.id.tv_email);
		tv_email.setText("Email: " + checkin.getUser().getEmail());

		TextView tv_description = (TextView) row
				.findViewById(R.id.tv_checkin_description);
		tv_description.setText(checkin.getDescription());

		/*
		 * float width = row.getWidth() -
		 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45,
		 * context.getResources().getDisplayMetrics());
		 */
		
		final WebView img_checkin = (WebView) row.findViewById(R.id.img_checkin);
		new Thread(new Runnable() {
			
			public void run() {
				String linkImage = "http://d.f14.photo.zdn.vn/upload/original/2012/10/24/14/44/1351064663357763639_574_574.jpg";
				// Toast.makeText(row.getContext(), tv_description.getWidth(),
				// Toast.LENGTH_LONG).show();
				String page = "<html><body><div align=\"center\"><img src=\""
						+ linkImage + "\" width=\"" + "200" + "\" /></div></body></html>";

				img_checkin.loadData(page, "text/html", "UTF-8");
				
			}
		}).start();
		
		// img_checkin.setImageDrawable(img);
		return row;
	}

	
}
