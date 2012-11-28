package com.nikmesoft.android.nearfood.components;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.activities.SearchActivity;
import com.nikmesoft.android.nearfood.activities.SearchItemActivity;
import com.nikmesoft.android.nearfood.activities.SearchMapActivity;
import com.nikmesoft.android.nearfood.activities.SearchTabGroupActivity;
import com.nikmesoft.android.nearfood.models.Place;

public class CustomItemizedOverlaySearch extends ItemizedOverlay {
	private List<OverlayItem> items;
	private ArrayList<Place> places;
	private Drawable marker;
	private SearchActivity activity;
	PopupWindow pw;
	Place place;

	public CustomItemizedOverlaySearch(SearchActivity activity, Drawable marker) {
		super(marker);
		this.activity = activity;
		this.marker = marker;
		items = new ArrayList<OverlayItem>();
		places = new ArrayList<Place>();
		boundCenterBottom(marker);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return (items.get(i));
	}

	@Override
	protected boolean onTap(int i) {
		if (places.get(i) != null) {
			place = places.get(i);
			activity.mc.animateTo(place.getMapPoint());
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Here x is the name of the xml which contains the popup components
			View view = inflater.inflate(activity.getIdPopup(), null);
			pw = new PopupWindow(view, activity.getWindowManager().getDefaultDisplay().getWidth()*2/3, activity.getWindowManager().getDefaultDisplay().getWidth()/2, true);
			pw.setAnimationStyle(android.R.style.Animation_Dialog);
			// Here y is the id of the root component
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			view.measure(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			@SuppressWarnings("deprecation")
			int screenWidth = activity.getWindowManager().getDefaultDisplay()
					.getWidth();
			@SuppressWarnings("deprecation")
			int screenHeight = activity.getWindowManager().getDefaultDisplay()
					.getHeight();
			@SuppressWarnings("unused")
			int x = screenWidth / 2 - view.getMeasuredWidth() / 2;
			
			@SuppressWarnings("unused")
			int y = screenHeight / 2 - view.getMeasuredHeight() / 2;
			
			@SuppressWarnings("unused")
			DisplayMetrics metrics = new DisplayMetrics();
			TextView popUpName = (TextView) view.findViewById(activity.getPopupName());
			popUpName.setText(place.getName());
			TextView popUpAddress = (TextView) view.findViewById(activity.getPopupAddress());
			popUpAddress.setText(place.getAddress());
			Button btnView = (Button) view
					.findViewById(activity.getIdBtnView());

			btnView.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					toActi(place);
					pw.dismiss();
				}
			});
			Button btnCancel = (Button) view
					.findViewById(activity.getIdBtnCancel());

			btnCancel.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					pw.dismiss();
				}
			});
			
			pw.showAtLocation(view, Gravity.NO_GRAVITY, (screenWidth - screenWidth*2/3)/2	, screenHeight/2);
			
		}
		return true;
	}

	public void toActi(Place place) {
		SearchTabGroupActivity parent = (SearchTabGroupActivity) activity
				.getParent();
		Intent intent = new Intent(parent, SearchItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtra("bundlePlace", bundle);
		parent.startNewActivity(SearchItemActivity.class.getSimpleName(),
				intent);
	}

	@Override
	public int size() {
		return (items.size());
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		boundCenterBottom(marker);
	}

	public void addItem(OverlayItem item, Place place) {
		items.add(item);
		places.add(place);
		populate();
	}

}
