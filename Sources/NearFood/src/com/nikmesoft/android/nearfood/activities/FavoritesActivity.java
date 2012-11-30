package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import com.google.android.maps.MapActivity;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.FavoriteResultAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchResultAdapter;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends MapActivity implements OnItemClickListener {

	private ListView lvSearch;
	protected FavoriteResultAdapter placeAdapter;
	protected ArrayList<Place> places;
	private ViewFlipper flipper;
	private int imgIndex = 1;
	private EditText ed_distance, ed_search;
	ArrayList<CheckBox> checkboxs;
	private Spinner spinner_distance;
	String str_filter = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		init();
	}

	private void init() {

		lvSearch = (ListView) findViewById(R.id.lvSearch);
		places = new ArrayList<Place>();
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "cung kha la do", 100));
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		placeAdapter = new FavoriteResultAdapter(this, R.layout.list_item_search,places);
		lvSearch.setAdapter(placeAdapter);
		lvSearch.setOnItemClickListener(this);
		flipper = (ViewFlipper) findViewById(R.id.details);
		checkboxs = new ArrayList<CheckBox>();
		ed_search = (EditText) findViewById(R.id.edt_search);
		ed_search.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				ed_search.setFocusableInTouchMode(true);
			}
		});
	}

	public void onClickListOrMap(View v) {
		AnimationFactory.flipTransition(flipper, FlipDirection.RIGHT_LEFT);
		ImageButton btnListMap = (ImageButton) v.findViewById(R.id.bt_listmap);
		if (imgIndex == 1) {
			imgIndex = 2;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_list));
		} else {
			imgIndex = 1;
			btnListMap.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.button_map));
		}
	}

	public void onClickLogin(View v) {

	}

	public void onClickFilter(View v) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getParent());
		View view = LayoutInflater.from(this).inflate(
				R.layout.menu_filter, null);
		Log.d("Viewwwwwwww", String.valueOf(view.getId()));
		final CheckBox check_distance = (CheckBox) view
				.findViewById(R.id.check_distance);
		final CheckBox check_location = (CheckBox) view
				.findViewById(R.id.check_location);
		final CheckBox check_place = (CheckBox) view
				.findViewById(R.id.check_place);
		final CheckBox check_dishes = (CheckBox) view
				.findViewById(R.id.check_dishes);
		ed_distance = (EditText) view.findViewById(R.id.ed_distance);
		spinner_distance = (Spinner) view.findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.distance_array,
				android.R.layout.simple_spinner_item);
		spinner_distance.setAdapter(adapter);
		alert.setTitle("Serach Filter");
		alert.setView(view);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				checkboxs.clear();
				str_filter = "";
				if (check_distance.isChecked()) {
					checkboxs.add(check_distance);
					str_filter += ed_distance.getText().toString()
							+ (String) spinner_distance.getSelectedItem()
									.toString() + " of "
							+ check_distance.getText().toString() + " - ";
				}
				if (check_location.isChecked()) {
					checkboxs.add(check_location);
					str_filter += check_location.getText().toString() + " - ";
				}
				if (check_place.isChecked()) {
					checkboxs.add(check_place);
					str_filter += check_place.getText().toString() + " - ";
				}
				if (check_dishes.isChecked()) {
					checkboxs.add(check_dishes);
					str_filter += check_dishes.getText().toString();
				}
				Toast.makeText(getApplicationContext(), str_filter,
						Toast.LENGTH_SHORT).show();

			}
		});
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		AlertDialog dialog = alert.create();
		dialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Place place = places.get(arg2);
		FavoritesTabGroupActivity parent = (FavoritesTabGroupActivity) getParent();
		Intent intent = new Intent(parent, FavoritesItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("place", place);
		intent.putExtra("bundlePlace", bundle);
		parent.startNewActivity(FavoritesItemActivity.class.getSimpleName(),
				intent);
		
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}
}
