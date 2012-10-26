package com.nikmesoft.android.nearfood.activities;


import java.util.ArrayList;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class ViewCheckInActivity extends BaseActivity implements ViewFactory {
	private Place place;
	LinearLayout layout;
	ArrayList<CheckIn> listCheckIn;
	ArrayList<Drawable> imgList;
	int positionShow;
	ImageSwitcher imageSwitcher;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@SuppressWarnings("deprecation")
	public View makeView() {
		ImageView iView = new ImageView(this);
		iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		iView.setBackgroundColor(0xFF000000);
		return iView;
	}

	private void init() {
		setContentView(R.layout.activity_view_checkin);

		this.place = (Place) getIntent().getExtras().get("place");

		TextView tv_placename = (TextView) findViewById(R.id.tv_place_name);
		tv_placename.setText(place.getName());

		TextView tv_placeaddress = (TextView) findViewById(R.id.tv_place_address);
		tv_placeaddress.setText(place.getAddress());

		TextView tv_description = (TextView) findViewById(R.id.tv_description);
		tv_description
				.setText("Khi thưởng thức ẩm thực, người ta thường nói: \"Món ngon không bằng lời đẹp\". Đến quán bánh tôm Bà Phúc, thực khách không chỉ được tự do lựa chọn món ăn theo sở thích mà còn được bà chủ giới thiệu tận tình về món bánh tôm và quan trọng hơn, là cách ăn như thế nào cho đúng cách.");

		User user = new User();
		user.setFullName("Phan Ngọc Tứ");
		user.setEmail("mr.shibuno@hotmail.com");
		user.setProfilePicture("http://icons.iconarchive.com/icons/artua/dragon-soft/256/User-icon.png");

		CheckIn checkIn = new CheckIn();
		checkIn.setUser(user);
		String description = "Khi thưởng thức ẩm thực, người ta thường nói: \"Món ngon không bằng lời đẹp\". Đến quán bánh tôm Bà Phúc, thực khách không chỉ được tự do lựa chọn món ăn theo sở thích mà còn được bà chủ giới thiệu tận tình về món bánh tôm và quan trọng hơn, là cách ăn như thế nào cho đúng cách.";

		listCheckIn = new ArrayList<CheckIn>();
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				CheckIn checkin = new CheckIn(
						1,
						description,
						"http://sphotos-d.ak.fbcdn.net/hphotos-ak-ash3/542972_378371868894102_1861606541_n.jpg");
				checkin.setUser(user);
				listCheckIn.add(checkin);
			} else {
				CheckIn checkin = new CheckIn(
						1,
						description,
						"http://sphotos-c.ak.fbcdn.net/hphotos-ak-ash4/252307_436532289703053_1987981101_n.jpg");
				checkin.setUser(user);
				listCheckIn.add(checkin);
			}

		}
		// adapter=new CheckInAdapter(this,
		// R.layout.activity_view_checkin_list_item, listCheckIn);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.img_switcher);
		imageSwitcher.setFactory(this);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right));
		imgList = new ArrayList<Drawable>();
		new Thread(new Runnable() {

			public void run() {
				try {
					Drawable placeimg = Utility.LoadImageFromWebOperations(
							place.getImagePath(), "check_in_place_img");

					//imageSwitcher.setImageDrawable(placeimg);
					imgList.add(placeimg);

				} catch (Exception e) {
				}
/*				for (int i = 0; i < listCheckIn.size(); i++) {
					try {
						Drawable drawable = Utility.LoadImageFromWebOperations(
								listCheckIn.get(i).getImagePath(),
								"check_in_img_top" + i);
						imgList.add(drawable);
						if (imgList.size() == 1) {
							imageSwitcher.setImageDrawable(drawable);
						}
					} catch (Exception e) {
					}
				}*/
			}
		}).start();
		positionShow = 0;

		layout = (LinearLayout) findViewById(R.id.scrollview_child);
		addView();
		changeImage(imageSwitcher);

	}

	public void changeImage(View v) {
		if (imgList.size() > 0) {
			if (positionShow < imgList.size()-1)
				positionShow++;
			else
				positionShow = 0;
			imageSwitcher.setImageDrawable(imgList.get(positionShow));
		}
	}

	public void addView() {
		for (int i = 0; i < listCheckIn.size(); i++)
			layout.addView(getView(i));

	}

	public View getView(final int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.activity_view_checkin_list_item,
				null);
		final CheckIn checkin = listCheckIn.get(position);

		TextView tv_username = (TextView) row.findViewById(R.id.tv_username);
		tv_username.setText(checkin.getUser().getFullName());

		TextView tv_email = (TextView) row.findViewById(R.id.tv_email);
		tv_email.setText("Email: " + checkin.getUser().getEmail());

		final ImageView img_avasta = (ImageView) row
				.findViewById(R.id.img_avasta);
		new Thread(new Runnable() {

			public void run() {

				try {
					Drawable drawable = Utility.LoadImageFromWebOperations(
							checkin.getUser().getProfilePicture(),
							"check_in_user_avasta" + position);
					img_avasta.setImageDrawable(drawable);
				} catch (Exception e) {
					img_avasta.setImageDrawable(getResources().getDrawable(
							R.drawable.default_user_icon));
				}

			}
		}).start();

		final ImageView img_checkin = (ImageView) row
				.findViewById(R.id.img_checkin);
		new Thread(new Runnable() {

			public void run() {

				try {
					Drawable drawable = Utility.LoadImageFromWebOperations(
							checkin.getImagePath(), "check_in_img" + position);
					//Drawable drawable = Utility.loadDrawable(
					//		checkin.getImagePath());
					img_checkin.setImageDrawable(drawable);
					imgList.add(drawable);
				} catch (Exception e) {
				}

			}
		}).start();

		TextView tv_description = (TextView) row
				.findViewById(R.id.tv_checkin_description);
		tv_description.setText(checkin.getDescription());

		return row;
	}
}
