package com.nikmesoft.android.nearfood.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.SearchResultAdapter;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.AnimationFactory;
import com.nikmesoft.android.nearfood.utils.AnimationFactory.FlipDirection;

@SuppressWarnings("deprecation")
@SuppressLint("ParserError")
public class SearchActivity extends BaseActivity implements OnItemClickListener {

	private ListView lvSearch;
	protected SearchResultAdapter placeAdapter;
	protected ArrayList<Place> places;
	private ViewFlipper flipper;
	private int imgIndex = 1;
	private EditText ed_distance, ed_search;
	ArrayList<CheckBox> checkboxs;
	private Spinner spinner_distance;
	String str_filter = "";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		init();
	}

	@SuppressLint("ParserError")
	public void init() {
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		places = new ArrayList<Place>();
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
		places.add(new Place("Quán Thanh Trúc",
				"958 Nguyễn Lương Bằng - Quan Lien chieu - da nang", "", 100));
		placeAdapter = new SearchResultAdapter(this, R.layout.list_item_search,
				places);
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

	@SuppressLint({ "ParserError", "NewApi", "NewApi", "NewApi" })
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
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this.getParent()).inflate(
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

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SearchTabGroupActivity parent = (SearchTabGroupActivity) getParent();
		parent.startNewActivity(SearchItemActivity.class.getSimpleName(),
				new Intent(parent, SearchItemActivity.class));
	}
}
