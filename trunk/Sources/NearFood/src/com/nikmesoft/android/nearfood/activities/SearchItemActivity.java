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
import com.nikmesoft.android.nearfood.models.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchItemActivity extends Activity {
	private ArrayList<Drawable> listImage;
	private ScrollView scrollview;
	private Gallery gallery;
	private ImageView left_arrow_imageview, right_arrow_imageview,
			selected_imageview;
	private int position_selected_imgview = 0;
	private GalleryAdapter galleryAdapter;
	private SearchCheckInResultAdapter checkInApdapter;
	private ListView lvCheckin;
	ArrayList<CheckIn> checkins;
	private LinearLayout linearlayout_list;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		init();
	}

	public void init() {

		// Adapter Check In
		linearlayout_list = (LinearLayout) findViewById(R.id.listview_layout_search);
		scrollview = (ScrollView) findViewById(R.id.ScrollViewLayout);
		
		checkins = new ArrayList<CheckIn>();
		checkins.add(new CheckIn(1, new User("Dang Cong Men", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number One", "","2 days ago"));
		checkins.add(new CheckIn(1, new User("Phan Ngoc Tu", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number two", "","2 days ago"));
		checkins.add(new CheckIn(1, new User("Dang Cong Men", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number One", "","2 days ago"));
		checkins.add(new CheckIn(1, new User("Phan Ngoc Tu", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number two", "","2 days ago"));
		checkins.add(new CheckIn(1, new User("Dang Cong Men", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number One", "","2 days ago"));
		checkins.add(new CheckIn(1, new User("Phan Ngoc Tu", "dcm.it.bkdn@gmail.com", "", ""), new Place("", "", "BÃ² kho Ä‘Æ°á»�ng Huá»³nh ThÃºc KhÃ¡ng, bÃ¡n buá»•i sÃ¡ng thÃ´i . Cáº¡nh quÃ¡n nÃ y buá»•i chiá»�u cÃ³ bÃ¡n bÃ¡nh canh, Äƒn cÅ©ng Ä‘Æ°á»£c.", 0), "Quan nay` Number two", "","2 days ago"));
		addItemListViewCustomer(checkins);
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
							.getDrawable(R.drawable.ic_arrow_left));
					right_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_arrow_right));
				}
				if (position_selected_imgview == 0) {
					left_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_nocolor));
				}
				if (position_selected_imgview == galleryAdapter.getCount() - 1) {
					right_arrow_imageview.setImageDrawable(getResources()
							.getDrawable(R.drawable.ic_nocolor));
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
					R.drawable.ic_nocolor));
			right_arrow_imageview.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_arrow_right));
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
					R.drawable.ic_nocolor));
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
					R.drawable.ic_nocolor));
		}
	}

	public void onClickBack(View v) {
		onBackPressed();
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
		SearchTabGroupActivity parent = (SearchTabGroupActivity)getParent();
		parent.startNewActivity(SearchMapActivity.class.getSimpleName(), new Intent(parent, SearchMapActivity.class));
	}
	public void addItemListViewCustomer(ArrayList<CheckIn> checks) {
		Log.d("Customer", "Da vao");
		for(int i=0; i<checks.size(); i++){
			LayoutInflater inflater = (LayoutInflater)getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(
					R.layout.list_item_checkin_search, null);
			TextView fullName = (TextView) row
					.findViewById(R.id.fullname_item_checkin_search);
			TextView description = (TextView) row
					.findViewById(R.id.description_item_checkin_search);
			TextView time = (TextView) row
					.findViewById(R.id.time_item_checkin_search);
			fullName.setText("Full Name");
			description.setText(checks.get(i).getDescription().toString());
			time.setText(checks.get(i).getTimeCheck().toString());
			linearlayout_list.addView(row);
		}
	}
}
