package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CheckInResultAdapter;
import com.nikmesoft.android.nearfood.models.Place;

@SuppressLint({ "NewApi" })
public class CheckInActivity extends BaseMapsActivity {
	EditText edt_search;
	private int selectedIndex;
	CheckInResultAdapter checkinResultAdapter;
	// private Drawable oldBackground;
	private Button bt_checkin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	private void init() {
		setContentView(R.layout.activity_checkin);
		bt_checkin = (Button) findViewById(R.id.bt_checkin);
		edt_search = (EditText) findViewById(R.id.edt_search);
		edt_search
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						return false;
					}
				});
		edt_search.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				v.setFocusableInTouchMode(true);

			}
		});
		ArrayList<Place> places = new ArrayList<Place>();
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));
		places.add(new Place("Bánh tôm Bà Phúc",
				"464/11 Trưng Nữ Vương - Đà Nẵng", "", "100"));

		ListView list = (ListView) findViewById(R.id.list);
		checkinResultAdapter = new CheckInResultAdapter(this,
				R.layout.activity_checkin_list_item, places);
		list.setAdapter(checkinResultAdapter);
		selectedIndex = -1;
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				if (selectedIndex == position) {
					// view.setBackground(oldBackground);
					selectedIndex = -1;
					bt_checkin.setVisibility(View.INVISIBLE);
					bt_checkin.setEnabled(false);
					((ImageView) view.findViewById(R.id.img_checked))
							.setImageDrawable(view.getContext().getResources()
									.getDrawable(R.drawable.ic_unselect));
					return;
				}
				if (selectedIndex >= 0)
					((ImageView) adapter.getChildAt(selectedIndex)
							.findViewById(R.id.img_checked))
							.setImageDrawable(view.getContext().getResources()
									.getDrawable(R.drawable.ic_unselect));
				// oldBackground=view.getBackground();

				// adapter.getChildAt(selectedIndex).setBackground(oldBackground);

				// view.setBackground(view.getContext().getResources().getDrawable(R.drawable.list_item_selected_background));
				// view.findViewById(R.id.img_checked).setVisibility(View.VISIBLE);
				bt_checkin.setVisibility(View.VISIBLE);
				bt_checkin.setEnabled(true);
				((ImageView) view.findViewById(R.id.img_checked))
						.setImageDrawable(view.getContext().getResources()
								.getDrawable(R.drawable.ic_selected));
				selectedIndex = position;
			}
		});
		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Intent intent = new Intent(view.getContext(),
						ViewCheckInActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("place",
						checkinResultAdapter.getItem(position));
				intent.putExtras(bundle);
				startActivity(intent);
				return false;
			}
		});
	}

	public void checkIn(View v) {
		Intent intent = new Intent(this, StartCheckInActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place",
				checkinResultAdapter.getItem(selectedIndex));
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void actionSearch(View v) {
		performSearch();
	}

	private void performSearch() {
	}

}
