package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CheckInAdapter;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.Utility;
import com.nikmesoft.android.nearfood.views.NestedListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewCheckInActivity extends BaseActivity {
	private Place place;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
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
		CheckIn checkIn = new CheckIn();
		checkIn.setUser(user);
		checkIn.setDescription("Khi thưởng thức ẩm thực, người ta thường nói: \"Món ngon không bằng lời đẹp\". Đến quán bánh tôm Bà Phúc, thực khách không chỉ được tự do lựa chọn món ăn theo sở thích mà còn được bà chủ giới thiệu tận tình về món bánh tôm và quan trọng hơn, là cách ăn như thế nào cho đúng cách.");

		ArrayList<CheckIn> listCheckIn = new ArrayList<CheckIn>();
		for(int i=0;i<10;i++)
			listCheckIn.add(checkIn);
		
		
		final ListView list = (ListView) findViewById(R.id.list);
		//Utility.setListViewHeightBasedOnChildren(list);
		list.setAdapter(new CheckInAdapter(this,
				R.layout.activity_view_checkin_list_item, listCheckIn));
		Utility.setListViewHeightBasedOnChildren(list);

	}
}
