package com.nikmesoft.android.nearfood.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class PointOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> items;
	private Drawable marker;
	private Context context;

	public PointOverlay(Context context, Drawable marker) {
		super(marker);
		this.context = context;
		this.marker = marker;
		items = new ArrayList<OverlayItem>();
		boundCenterBottom(marker);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return (items.get(i));
	}

	@Override
	protected boolean onTap(int i) {
		Toast.makeText(context, items.get(i).getSnippet(), Toast.LENGTH_SHORT)
				.show();
		return (true);
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

	public void addItem(OverlayItem item) {
		items.add(item);
		populate();
	}
}