package com.nikmesoft.android.nearfood.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {
	private ArrayList<Drawable> listImage;
	private Activity context;
	private ImageView imgview;
	private ViewHolder holder;

	public GalleryAdapter(Activity main, ArrayList<Drawable> listImage) {
		this.context = main;
		this.listImage = listImage;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return this.listImage.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			imgview = new ImageView(this.context);
			imgview.setPadding(2, 2, 2, 2);
			convertView = imgview;
			holder = new ViewHolder();
			holder.imgView = imgview;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgView.setImageDrawable(listImage.get(position));
		holder.imgView.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.imgView.setLayoutParams(new Gallery.LayoutParams(150, 90));

		return imgview;
	}

	private static class ViewHolder {
		ImageView imgView;
	}

}
