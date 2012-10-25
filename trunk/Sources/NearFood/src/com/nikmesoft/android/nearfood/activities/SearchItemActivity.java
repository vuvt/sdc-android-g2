package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.R.layout;
import com.nikmesoft.android.nearfood.R.menu;
import com.nikmesoft.android.nearfood.adapters.GalleryAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchCheckInResultAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchResultAdapter;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SearchItemActivity extends Activity {
	private ArrayList<Drawable> listImage;
	private Gallery gallery;
	private ImageView left_arrow_imageview, right_arrow_imageview,
			selected_imageview;
	private int position_selected_imgview = 0;
	private GalleryAdapter galleryAdapter;
	private SearchCheckInResultAdapter checkInApdapter;
	private ListView lvCheckin;
	ArrayList<CheckIn> checkins;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		init();
	}

	public void init() {

		// Adapter Check In
		lvCheckin = (ListView) findViewById(R.id.lv_checkin_search);
		lvCheckin.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true; // Indicates that this has been handled by you
									// and will not be forwarded further.
				}
				return false;
			}
		});
		checkins = new ArrayList<CheckIn>();
		checkins.add(new CheckIn(
				1,
				2,
				3,
				"Bò kho đường Huỳnh Thúc Kháng, bán buổi sáng thôi . Cạnh quán này buổi chiều có bán bánh canh, ăn cũng được.",
				"asd", "2 days ago"));
		checkins.add(new CheckIn(
				2,
				2,
				3,
				"Bò kho đường Huỳnh Thúc Kháng, bán buổi sáng thôi . Cạnh quán này buổi chiều có bán bánh canh, ăn cũng được.",
				"asd", "2 days ago"));
		checkins.add(new CheckIn(
				3,
				2,
				3,
				"Bò kho đường Huỳnh Thúc Kháng, bán buổi sáng thôi . Cạnh quán này buổi chiều có bán bánh canh, ăn cũng được.",
				"asd", "2 days ago"));
		checkInApdapter = new SearchCheckInResultAdapter(this,
				R.layout.list_item_checkin_search, checkins);
		lvCheckin.setAdapter(checkInApdapter);
		// Adapter Gallery
		gallery = (Gallery) findViewById(R.id.gallery_search);
		left_arrow_imageview = (ImageView) findViewById(R.id.left_arrow_imageview);
		right_arrow_imageview = (ImageView) findViewById(R.id.right_arrow_imageview);
		selected_imageview = (ImageView) findViewById(R.id.selected_imageview);
		getDrawable();
		galleryAdapter = new GalleryAdapter(this, listImage);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				position_selected_imgview = arg2;
				if (position_selected_imgview > 0
						&& position_selected_imgview < galleryAdapter
								.getCount() - 1) {
					left_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.arrow_left_enabled));
					right_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.arrow_right_enabled));
				}
				if (position_selected_imgview == 0) {
					left_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.arrow_left_disabled));
				}
				if (position_selected_imgview == galleryAdapter.getCount() - 1) {
					right_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.arrow_right_disabled));
				}
				setImageView(position_selected_imgview);
				changeBorderForSelectedImage(position_selected_imgview);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (listImage.size() > 0) {
			left_arrow_imageview.setImageDrawable(getResources().getDrawable(
					R.drawable.arrow_left_disabled));
			right_arrow_imageview.setImageDrawable(getResources().getDrawable(
					R.drawable.arrow_right_enabled));
		}
		gallery.setAdapter(galleryAdapter);

	}

	public void setImageView(int position) {
		BitmapDrawable bd = (BitmapDrawable) listImage.get(position);
		Bitmap b = Bitmap.createScaledBitmap(bd.getBitmap(),
				(int) (bd.getIntrinsicHeight() * 0.9),
				(int) (bd.getIntrinsicWidth() * 0.7), false);
		selected_imageview.setImageBitmap(b);
		selected_imageview.setScaleType(ScaleType.FIT_XY);
	}

	private void changeBorderForSelectedImage(int selectedItemPos) {

		int count = gallery.getChildCount();

		for (int i = 0; i < count; i++) {

			ImageView imageView = (ImageView) gallery.getChildAt(i);
			imageView.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.image_border));
			imageView.setPadding(3, 3, 3, 3);

		}

		ImageView imageView = (ImageView) gallery.getSelectedView();
		imageView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.selected_image_border));
		imageView.setPadding(3, 3, 3, 3);
	}

	public void getDrawable() {
		listImage = new ArrayList<Drawable>();
		listImage.add(getResources().getDrawable(R.drawable.natureimage1));
		listImage.add(getResources().getDrawable(R.drawable.natureimage2));
		listImage.add(getResources().getDrawable(R.drawable.natureimage3));
		listImage.add(getResources().getDrawable(R.drawable.natureimage4));

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_search_item, menu);
		return true;
	}

	public void onClickLeftArrow(View v) {
		if (position_selected_imgview > 0)
			position_selected_imgview--;
		gallery.setSelection(position_selected_imgview);
		setImageView(position_selected_imgview);
		changeBorderForSelectedImage(position_selected_imgview);
		if (position_selected_imgview == 0) {
			left_arrow_imageview.setImageDrawable(getResources().getDrawable(
					R.drawable.arrow_left_disabled));
		}

	}

	public void onClickRightArrow(View v) {
		if (position_selected_imgview < galleryAdapter.getCount() - 1)
			position_selected_imgview++;
		gallery.setSelection(position_selected_imgview);
		setImageView(position_selected_imgview);
		changeBorderForSelectedImage(position_selected_imgview);
		if (position_selected_imgview == galleryAdapter.getCount() - 1) {
			right_arrow_imageview.setImageDrawable(getResources().getDrawable(
					R.drawable.arrow_right_disabled));
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.i("Vao` chua?", "Da vao");
		Intent intent = new Intent(this, SearchActivity.class);
		SearchActivity parentActivity = (SearchActivity) getParent();
		parentActivity.replaceContentView("SearchActivity", intent,
				Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	}

	public void onClickBack(View v) {
		Intent intent = new Intent(this, SearchActivity.class);
		SearchActivity parentActivity = (SearchActivity) getParent();
		parentActivity.replaceContentView("SearchActivity", intent,
				Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	}

	public void onClickFacebook(View v) {

	}

	public void onClickFavorite(View v) {

	}

	public void onClickLike(View v) {

	}

	public void onClickCheckInSearch(View v) {

	}

	public void onClickMap(View v) {

	}
}
