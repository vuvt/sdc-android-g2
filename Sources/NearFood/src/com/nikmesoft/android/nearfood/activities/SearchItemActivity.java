package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.GalleryAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchCheckInResultAdapter;
import com.nikmesoft.android.nearfood.binding.ExtraBinding;
import com.nikmesoft.android.nearfood.handlers.AddFavoriteHander;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.GetCheckInsOfPlaceHander;
import com.nikmesoft.android.nearfood.handlers.GetPlaceHander;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.HttpFileUploader;
import com.nikmesoft.android.nearfood.utils.Utilities;

import android.R.bool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.BaseKeyListener;
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
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class SearchItemActivity extends Activity {
	Facebook facebook = new Facebook("302093569905424");
	private SharedPreferences mPrefs;
	private Button btnShare, btnShareOnFacebook, btnCancel, btnCheckInSearch;
	private TextView nameFacebook, namePlace, link, description, noteShare,
			search_name_place, address_information, description_information;
	private EditText contentShare;
	Dialog dialog_share;
	ProgressBar dialog_progressBar;
	private ProgressDialog mProgress, progress_favorite, progress_check_favorite;
	private ArrayList<Drawable> listImage;
	private ScrollView scrollview;
	private Gallery gallery;
	private ImageView left_arrow_imageview, right_arrow_imageview, imgFacebook,
			selected_imageview, separator;
	private int position_selected_imgview = 0;
	private GalleryAdapter galleryAdapter;
	private SearchCheckInResultAdapter checkInApdapter;
	private ListView lvCheckin;
	ArrayList<CheckIn> checkins;
	private LinearLayout linearlayout_list;
	private ProgressDialog progressDialog;
	private Place place;
	private ImageButton imgFavorite, imgLike;
	private Handler hander = new Handler();
	private int page = 1, totalPage = 1, pagePlaces = 1, totalPagePlaces = 1;
	private ArrayList<Place> places = new ArrayList<Place>();
	private boolean isFavorited = false, isLike = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		init();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void init() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundlePlace");
		place = (Place) bundle.getSerializable("place");
		Log.d("Information",
				String.valueOf(place.getName() + "  " + place.getAddress()));
		search_name_place = (TextView) findViewById(R.id.search_name_place);
		search_name_place.setText(place.getName());
		address_information = (TextView) findViewById(R.id.address_information);
		address_information.setText(place.getAddress());
		description_information = (TextView) findViewById(R.id.description_information);
		description_information.setText(place.getDescription());
		imgFavorite = (ImageButton) findViewById(R.id.titlebar_favorite);
		Log.d("Favorited : ", String.valueOf(place.isFavorited()));
		if(place.isFavorited()){
			isFavorited = true;
			imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unfavorite));
			}
		else {
			isFavorited = false;
			imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
		}
		imgLike = (ImageButton) findViewById(R.id.titlebar_like);
		if(place.isLiked()){
			isLike = true;
			imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unlike));
		}
		else {
			isLike = false;
			imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_like));
		}
		
		
		// Adapter Check In
		linearlayout_list = (LinearLayout) findViewById(R.id.listview_layout_search);
		scrollview = (ScrollView) findViewById(R.id.ScrollViewLayout);

		checkins = new ArrayList<CheckIn>();
		addItemListViewCustomer(checkins);
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage("Loading. Please wait...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		WSLoader ws = new WSLoader();
		ws.execute();
		// Adapter Gallery
		listImage = new ArrayList<Drawable>();
		gallery = (Gallery) findViewById(R.id.gallery_search);
		separator = (ImageView) findViewById(R.id.separator);
		left_arrow_imageview = (ImageView) findViewById(R.id.left_arrow_imageview);
		right_arrow_imageview = (ImageView) findViewById(R.id.right_arrow_imageview);
		selected_imageview = (ImageView) findViewById(R.id.selected_imageview);
		galleryAdapter = new GalleryAdapter(this, listImage);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.d("Position", String.valueOf(arg2));
				Log.d("Count", String.valueOf(galleryAdapter.getCount()));
				position_selected_imgview = arg2;
				if (position_selected_imgview > 0
						&& position_selected_imgview < (galleryAdapter
								.getCount() - 1)) {
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
		gallery.setAdapter(galleryAdapter);
		
		BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				places = new ArrayList<Place>();
				WSLoaderGetTotalPagePlaces ws = new WSLoaderGetTotalPagePlaces();
				ws.execute();
			}   
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.nikmesoft.android.nearfood.activities.LATER_LOGIN_BROADCAST");
			registerReceiver(receiver, filter);
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

	public void onClickFavorite(View v) {
		if (MyApplication.USER_CURRENT != null) {
			if(isFavorited){
				isFavorited = false;
				RemoveFavorite removeFavorite = new RemoveFavorite();
				removeFavorite.execute((int) MyApplication.USER_CURRENT.getId(),
						place.getId());
			}
			else {
			isFavorited = true;
			AddFavorite addFavorite = new AddFavorite();
			addFavorite.execute((int) MyApplication.USER_CURRENT.getId(),
					place.getId());
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(SearchItemActivity.this.getParent());
			builder.setMessage("Please to login before you using this feature").setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							MyApplication.isSwitchTabLoginChild = true;
							MyApplication.tabCurrent = 0;
							Intent intent = new Intent();
							intent.setClass(getParent(), LoginActivity.class);
							getParent().startActivity(intent);
						}

					});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public void onClickLike(View v) {
		if (MyApplication.USER_CURRENT != null) {
			if(isLike){
				isLike = false;
				LikeStatus ls = new LikeStatus();
				ls.execute((int) MyApplication.USER_CURRENT.getId(),
						place.getId(),-1);
			}
			else {
			isLike = true;
			LikeStatus ls = new LikeStatus();
			ls.execute((int) MyApplication.USER_CURRENT.getId(),
					place.getId(),1);
			}
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(SearchItemActivity.this.getParent());
			builder.setMessage("Please to login before you using this feature.").setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							MyApplication.isSwitchTabLoginChild = true;
							MyApplication.tabCurrent = 0;
							Intent intent = new Intent();
							intent.setClass(getParent(), LoginActivity.class);
							getParent().startActivity(intent);
						}

					});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	public void onClickCheckInSearch(View v) {
		if (MyApplication.USER_CURRENT != null) {
			MyApplication.isSwitchTab = true;
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("idPlace", place.getId());
			bundle.putString("namePlace", place.getName());
			bundle.putString("addressPlace", place.getAddress());
			bundle.putString("phonePlace", place.getPhoneNumber());
			bundle.putString("descriptionPlace", place.getDescription());
			bundle.putString("referenceKeyPlace", place.getReferenceKey());
			bundle.putInt("longitudePlace", place.getMapPoint().getLongitudeE6());
			bundle.putInt("latitudePlace", place.getMapPoint().getLatitudeE6());
			intent.putExtra("bundlePlace", bundle);
			MyApplication.tabHost.setCurrentTab(1);
			intent.setAction("com.nikmesoft.android.nearfood.activities.DATA_BROADCAST");
			sendBroadcast(intent);} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(SearchItemActivity.this.getParent());
				builder.setMessage("Please to login before you using this feature.").setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MyApplication.isSwitchTabLoginChild = true;
								MyApplication.tabCurrent = 0;
								Intent intent = new Intent();
								intent.setClass(getParent(), LoginActivity.class);
								getParent().startActivity(intent);
							}
	
						});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
	}

	public void onClickMap(View v) {
		/*
		 * SearchTabGroupActivity parent = (SearchTabGroupActivity)getParent();
		 * parent.startNewActivity(SearchMapActivity.class.getSimpleName(), new
		 * Intent(parent, SearchMapActivity.class));
		 */
		Intent intent = new Intent(this.getParent(), SearchMapActivity.class);
		intent.putExtra("Latitude", place.getMapPoint().getLatitudeE6());
		intent.putExtra("Longitude", place.getMapPoint().getLongitudeE6());
		intent.putExtra("NamePlace", place.getName());
		intent.putExtra("AddressPlace", place.getAddress());
		startActivity(intent);
	}

	public void addItemListViewCustomer(ArrayList<CheckIn> checks) {
		for (int i = 0; i < checks.size(); i++) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View row = inflater
					.inflate(R.layout.list_item_checkin_search, null);
			TextView fullName = (TextView) row
					.findViewById(R.id.fullname_item_checkin_search);
			fullName.setText(checks.get(i).getUser().getFullName());
			TextView description = (TextView) row
					.findViewById(R.id.description_item_checkin_search);
			description.setText(checks.get(i).getDescription());
			TextView time = (TextView) row
					.findViewById(R.id.time_item_checkin_search);
			time.setText(checks.get(i).getTimeCheck());
			description.setText(checks.get(i).getDescription().toString());
			time.setText(checks.get(i).getTimeCheck().toString());
			linearlayout_list.addView(row);
		}
	}

	public void onClickFacebook(View v) {
		dialog_share = new Dialog(this.getParent());
		dialog_share.setContentView(R.layout.share_face);
		dialog_share.setTitle("Share facebook");
		noteShare = (TextView) dialog_share.findViewById(R.id.noteShare);
		dialog_progressBar = (ProgressBar) dialog_share
				.findViewById(R.id.dialogProgressBar);

		imgFacebook = (ImageView) dialog_share.findViewById(R.id.imgFacebook);
		Drawable draw = getResources().getDrawable(R.drawable.ic_launcher);
		if (listImage.size() > 0)
			draw = listImage.get(0);
		imgFacebook.setImageBitmap(Utilities.drawableToBitmap(draw));
		btnShare = (Button) dialog_share.findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noteShare.setVisibility(View.GONE);
				btnShareOnFacebook.setVisibility(View.GONE);
				btnShare.setVisibility(View.GONE);
				contentShare.setVisibility(View.VISIBLE);
				dialog_progressBar.setVisibility(View.VISIBLE);
				btnCancel.setVisibility(View.VISIBLE);
				shareOnFacebook();
			}
		});
		contentShare = (EditText) dialog_share.findViewById(R.id.contentShare);
		namePlace = (TextView) dialog_share.findViewById(R.id.namePlace);
		namePlace.setText(place.getName());
		link = (TextView) dialog_share.findViewById(R.id.link);
		description = (TextView) dialog_share.findViewById(R.id.description);
		btnShareOnFacebook = (Button) dialog_share
				.findViewById(R.id.btnShareOnFacebook);
		btnCancel = (Button) dialog_share.findViewById(R.id.btnCancel);
		btnCancel.setEnabled(true);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				dialog_share.dismiss();
				dialog_progressBar.setVisibility(View.GONE);
			}
		});
		btnShareOnFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				noteShare.setVisibility(View.GONE);
				btnShareOnFacebook.setVisibility(View.GONE);
				btnShare.setVisibility(View.VISIBLE);
				contentShare.setVisibility(View.VISIBLE);
				dialog_progressBar.setVisibility(View.GONE);
				btnCancel.setVisibility(View.VISIBLE);
			}
		});
		dialog_share
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
		dialog_share.show();
	}

	@SuppressWarnings({ "deprecation", "deprecation", "deprecation" })
	public void shareOnFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {

			facebook.authorize(this.getParent(), new String[] {}, -1,
					new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							postPhotoWithMsg();
						}

						@Override
						public void onFacebookError(FacebookError error) {

							Log.e("Facebook", "Error");
							SearchItemActivity.this
									.runOnUiThread(new Runnable() {
										public void run() {
											dialog_progressBar
													.setVisibility(View.GONE);
											btnShare.setVisibility(View.GONE);
											noteShare
													.setVisibility(View.VISIBLE);
											noteShare
													.setText("No connected to Facebook.");
											btnShareOnFacebook
													.setVisibility(View.VISIBLE);
											btnCancel
													.setVisibility(View.VISIBLE);
										}
									});

						}

						@Override
						public void onError(DialogError e) {

						}

						@Override
						public void onCancel() {
							Log.e("Facebook", "Error");
							SearchItemActivity.this
									.runOnUiThread(new Runnable() {
										public void run() {
											dialog_progressBar
													.setVisibility(View.GONE);
											btnShare.setVisibility(View.GONE);
											btnShareOnFacebook
													.setVisibility(View.VISIBLE);
											btnCancel
													.setVisibility(View.VISIBLE);
										}
									});
						}
					});
		} else {
			postPhotoWithMsg();
		}

	}

	public void postPhotoWithMsg() {
		if (Utilities.isNetworkAvailable(getApplicationContext())) {

			byte[] data = null;
			// data = Utilities.getBytes(bitmap);
			Bundle params = new Bundle();
			/*
			 * params.putString("message", msg); params.putByteArray("picture",
			 * data)
			 */;
			params.putString("message", contentShare.getText().toString());
			Log.d("Message", contentShare.getText().toString());
			params.putString("name", namePlace.getText().toString());
			params.putString("link", link.getText().toString());
			params.putString("description", description.getText().toString());
			params.putString("picture", "http://twitpic.com/show/thumb/6hqd44");
			btnCancel.setEnabled(false);
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
			mAsyncRunner.request("me/feed", params, "POST",
					new SampleUploadListener(), null);
		} else {
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					dialog_progressBar.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("Network not available");
					btnShareOnFacebook.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});

		}

	}

	public class SampleUploadListener extends BaseKeyListener implements
			RequestListener {

		public void onComplete(final String response, final Object state) {
			Log.d("Facebook", "Response: " + response.toString());
			Log.d("Message", contentShare.getText().toString());
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					btnCancel.setEnabled(true);
					dialog_progressBar.setVisibility(View.GONE);
					contentShare.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("Shared");
					btnShareOnFacebook.setVisibility(View.GONE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});

		}

		public void onFacebookError(FacebookError e, Object state) {

			Log.e("Facebook", "Error");
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					dialog_progressBar.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("No connected to Facebook.");
					btnShareOnFacebook.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});

		}

		public Bitmap getInputType(Bitmap img) {

			return img;
		}

		public int getInputType() {

			return 0;
		}

		public void onIOException(IOException e, Object state) {
			Log.e("Facebook", "Error");
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					dialog_progressBar.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("No connected to Facebook.");
					btnShareOnFacebook.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});
		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.e("Facebook", "Error");
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					dialog_progressBar.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("Error!");
					btnShareOnFacebook.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});
		}

		public void onMalformedURLException(MalformedURLException e,
				Object state) {

			Log.e("Facebook", "Error");
			SearchItemActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					dialog_progressBar.setVisibility(View.GONE);
					btnShare.setVisibility(View.GONE);
					noteShare.setVisibility(View.VISIBLE);
					noteShare.setText("No connected to Facebook.");
					btnShareOnFacebook.setVisibility(View.VISIBLE);
					btnCancel.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	private Object xmlParser(String strXml) {
		byte xmlBytes[] = strXml.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				xmlBytes);
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxPF.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();
			GetCheckInsOfPlaceHander handler = new GetCheckInsOfPlaceHander();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(byteArrayInputStream));
			return handler.getResult();

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	private Object xmlParserGetPlaces(String strXml) {
		byte xmlBytes[] = strXml.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				xmlBytes);
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxPF.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();
			GetPlaceHander handler = new GetPlaceHander();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(byteArrayInputStream));
			totalPagePlaces = handler.getTotalPage();
			return handler.getResult();

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	private Object xmlParserAddFavorit(String strXml) {
		byte xmlBytes[] = strXml.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				xmlBytes);
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxPF.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();
			AddFavoriteHander handler = new AddFavoriteHander();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(byteArrayInputStream));
			return handler.getResult();

		} catch (ParserConfigurationException ex) {
			ex.printStackTrace();
		} catch (SAXException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	public String requests() {

		return null;
	}

	Runnable getImages = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for (CheckIn check : checkins) {
				try {
					Log.d("img", "img1");
					listImage.add(Utilities.LoadImageFromWebOperations(
							check.getImagePath(), check.getImagePath()));
					Log.d("img", "img2");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			galleryAdapter.notifyDataSetChanged();
		}
	};

	private class WSLoader extends AsyncTask<String, Integer, Object> {
		@Override
		protected Object doInBackground(String... params) {
			String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ " <soapenv:Header/>"
					+ " <soapenv:Body>"
					+ " <getCheckIns soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ " <GetCheckInsRequest xsi:type=\"sfo:GetCheckInsRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ " <!--You may enter the following 2 items in any order-->"
					+ " <id_place xsi:type=\"xsd:int\">"
					+ String.valueOf(place.getId())
					+ "</id_place>"
					+ " <page xsi:type=\"xsd:int\">?</page>"
					+ " </GetCheckInsRequest>"
					+ " </getCheckIns>"
					+ " </soapenv:Body>" + " </soapenv:Envelope>";
			String soapAction = "http://nikmesoft.com/apis/SFoodServices/index.php/getPlaces";
			return xmlParser(Utilities.callWS(request, soapAction));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getParent().finish();
							}
						});
				builder.show();
			} else {
				if (result.getClass().equals(ErrorCode.class)) {
				} else {
					checkins.clear();
					for (CheckIn checkin : ((ArrayList<CheckIn>) result))
						checkins.add(checkin);
					linearlayout_list.removeAllViews();
					addItemListViewCustomer(checkins);

				}
				progressDialog.dismiss();
				getImages imgs = new getImages();
				imgs.execute((ArrayList<CheckIn>) result);
			}
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
	private class WSLoaderGetPlaces extends AsyncTask<Void, Integer, Object> {
		@Override
		protected Object doInBackground(Void... params) {
			String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<getPlaces soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ "<GetPlacesRequest xsi:type=\"sfo:GetPlacesRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ "<!--You may enter the following 9 items in any order-->"
					+ "<id_user xsi:type=\"xsd:int\">" + MyApplication.USER_CURRENT.getId() + "</id_user>"
					+ "<latitude xsi:type=\"xsd:double\">108</latitude>"
					+ "<longitude xsi:type=\"xsd:double\">16</longitude>"
					+ "<filter_distance xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(0).isChecked())
					+ "</filter_distance>"
					+ "<distance xsi:type=\"xsd:double\">"
					+ String.valueOf(MyApplication.distance)
					+ "</distance>"
					+ "<filter_address xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(1).isChecked())
					+ "</filter_address>"
					+ "<filter_name xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(2).isChecked())
					+ "</filter_name>"
					+ "<filter_dishes xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(3).isChecked())
					+ "</filter_dishes>"
					+ "<key xsi:type=\"xsd:string\">"
					+ MyApplication.contentSearch
					+ "</key>"
					+ "<page xsi:type=\"xsd:int\">"
					+ pagePlaces
					+ "</page>"
					+ "</GetPlacesRequest>"
					+ "</getPlaces>"
					+ "</soapenv:Body>" + "</soapenv:Envelope>";
			String soapAction = "http://nikmesoft.com/apis/SFoodServices/index.php/getPlaces";
			return xmlParserGetPlaces(Utilities.callWS(request, soapAction));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result==null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				builder.show();
			} else {
				if (result.getClass().equals(ErrorCode.class)) {
				} else {
					ArrayList<Place> array = new ArrayList<Place>();
					array = (ArrayList<Place>)result;
					for(int i=0;i< array.size(); i++){
						places.add(array.get(i));
					}
					if(pagePlaces==totalPagePlaces){
						while(true){
							if((8*(pagePlaces-1) + array.size()) == places.size())break;
						}
						for(int i =0 ; i< places.size(); i++){
							if(places.get(i).getId()==place.getId()){
								if(places.get(i).isLiked()){
									isLike = true;
									imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unlike));
								}
								else	{
									isLike = false;
									imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_like));
								}
								if(places.get(i).isFavorited()){
									isFavorited = true;
									imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unfavorite));
								}
								else {
									isFavorited = false;
									imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
								}
								progressDialog.dismiss();
								progressDialog.setMessage("Loading. Please wait...");
								break;
							}
						}
					}
				}
				
			}
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
	private class getImages extends
			AsyncTask<ArrayList<CheckIn>, Integer, ArrayList<Drawable>> {

		@Override
		protected ArrayList<Drawable> doInBackground(
				ArrayList<CheckIn>... params) {
			for (CheckIn checkIn : params[0]) {
				try {
					listImage.add(Utilities.LoadImageFromWebOperations(
							checkIn.getImagePath(), checkIn.getImagePath()));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Drawable> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (listImage.size() == 0) {
				right_arrow_imageview.setVisibility(View.GONE);
				left_arrow_imageview.setVisibility(View.GONE);
				gallery.setVisibility(View.GONE);
				separator.setVisibility(View.GONE);
			} else {
				right_arrow_imageview.setVisibility(View.VISIBLE);
				left_arrow_imageview.setVisibility(View.VISIBLE);
				gallery.setVisibility(View.VISIBLE);
				separator.setVisibility(View.VISIBLE);
			}
			galleryAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

	}

	public class MyArrayList extends ArrayList<CheckIn> implements Serializable {
		public MyArrayList() {
			super();
		}

	}

	private class AddFavorite extends AsyncTask<Integer, Integer, Object> {

		@Override
		protected Object doInBackground(Integer... params) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<addFavorite soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ "<AddFavoriteRequest xsi:type=\"sfo:AddFavoriteRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ "<!--You may enter the following 2 items in any order-->"
					+ "<id_user xsi:type=\"xsd:int\">"
					+ params[0]
					+ "</id_user>"
					+ "<id_place xsi:type=\"xsd:int\">"
					+ params[1]
					+ "</id_place>"
					+ "</AddFavoriteRequest>"
					+ "</addFavorite>"
					+ "</soapenv:Body>"
					+ "</soapenv:Envelope>";
			return xmlParserAddFavorit(Utilities
					.callWS(body,
							"http://nikmesoft.com/apis/SFoodServices/index.php/addFavorite"));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				builder.show();
			}
			{
				if (result != null && result.getClass().equals(ErrorCode.class)) {
					progress_favorite.dismiss();
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),
							((ErrorCode) result).getErrorMsg());
				} else {
					progress_favorite.dismiss();
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),"You added successfully!");
					imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unfavorite));
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress_favorite = new ProgressDialog(
					SearchItemActivity.this.getParent());
			progress_favorite.setMessage("You are adding to your favorite...Please wait!");
			progress_favorite.setCancelable(false);
			progress_favorite.show();
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
	private class RemoveFavorite extends AsyncTask<Integer, Integer, Object>{

		@Override
		protected Object doInBackground(Integer... params) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						   + " <soapenv:Header/>"
						   + " <soapenv:Body>"
						      + " <removeFavorite soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
						    	  + " <RemoveFavoriteRequest xsi:type=\"sfo:RemoveFavoriteRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
						    		  + " <!--You may enter the following 2 items in any order-->"
						            + " <id_user xsi:type=\"xsd:int\">" + params[0] + "</id_user>"
						            + " <id_place xsi:type=\"xsd:int\">" + params[1] + "</id_place>"
						            + " </RemoveFavoriteRequest>"
						      + " </removeFavorite>"
						   + " </soapenv:Body>"
						+ " </soapenv:Envelope>";
			return xmlParserAddFavorit(Utilities
					.callWS(body,
							"http://nikmesoft.com/apis/SFoodServices/index.php/removeFavorite"));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				builder.show();
			}
			{
				if (result != null && result.getClass().equals(ErrorCode.class)) {
					progress_favorite.dismiss();
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),
							((ErrorCode) result).getErrorMsg());
				} else {
					progress_favorite.dismiss();
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),"You removed successfully!");
					imgFavorite.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_favorite));
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress_favorite = new ProgressDialog(
					SearchItemActivity.this.getParent());
			progress_favorite.setMessage("You are removing out of your favorite...Please wait!");
			progress_favorite.setCancelable(false);
			progress_favorite.show();
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
	private class LikeStatus extends AsyncTask<Integer, Integer, Object>{

		@Override
		protected Object doInBackground(Integer... params) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
							   + "<soapenv:Header/>"
							   + "<soapenv:Body>"
							      + "<addFB soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
							    	  + "<AddFBRequest xsi:type=\"sfo:AddFBRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
							    		  + "<!--You may enter the following 3 items in any order-->"
							            + "<id_user xsi:type=\"xsd:int\">" + params[0] + "</id_user>"
							            + "<id_place xsi:type=\"xsd:int\">" + params[1] +"</id_place>"
							            + "<action xsi:type=\"xsd:int\">" + params[2] + "</action>"
							            + "</AddFBRequest>"
							      + "</addFB>"
							   + "</soapenv:Body>"
							+ "</soapenv:Envelope>";
			return xmlParserAddFavorit(Utilities
					.callWS(body,
							"http://nikmesoft.com/apis/SFoodServices/index.php/addFB"));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				builder.show();
			}
			{
				if (result != null && result.getClass().equals(ErrorCode.class)) {
					progress_favorite.dismiss();
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),
							((ErrorCode) result).getErrorMsg());
				} else {
					progress_favorite.dismiss();
					if(isLike ==true){
					CommonUtil.dialogNotify(
							SearchItemActivity.this.getParent(),"You liked.");
					imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_unlike));
					}
					else{
						CommonUtil.dialogNotify(
								SearchItemActivity.this.getParent(),"You unliked.");
						imgLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_like));
					}
					
				}
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress_favorite = new ProgressDialog(
					SearchItemActivity.this.getParent());
			if(isLike ==true){
			progress_favorite.setMessage("You are liking this place...Please wait!");
			}
			else progress_favorite.setMessage("You are unliking this place...Please wait!");
			progress_favorite.setCancelable(false);
			progress_favorite.show();
			
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}
	private class WSLoaderGetTotalPagePlaces extends AsyncTask<Void, Integer, Object> {
		@Override
		protected Object doInBackground(Void... params) {
			String request = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<getPlaces soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ "<GetPlacesRequest xsi:type=\"sfo:GetPlacesRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ "<!--You may enter the following 9 items in any order-->"
					+ "<id_user xsi:type=\"xsd:int\">" +  MyApplication.USER_CURRENT.getId() + "</id_user>"
					+ "<latitude xsi:type=\"xsd:double\">108</latitude>"
					+ "<longitude xsi:type=\"xsd:double\">16</longitude>"
					+ "<filter_distance xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(0).isChecked())
					+ "</filter_distance>"
					+ "<distance xsi:type=\"xsd:double\">"
					+ String.valueOf(MyApplication.distance)
					+ "</distance>"
					+ "<filter_address xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(1).isChecked())
					+ "</filter_address>"
					+ "<filter_name xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(2).isChecked())
					+ "</filter_name>"
					+ "<filter_dishes xsi:type=\"xsd:boolean\">"
					+ String.valueOf(MyApplication.checkboxs.get(3).isChecked())
					+ "</filter_dishes>"
					+ "<key xsi:type=\"xsd:string\">"
					+ MyApplication.contentSearch
					+ "</key>"
					+ "<page xsi:type=\"xsd:int\">"
					+ pagePlaces++
					+ "</page>"
					+ "</GetPlacesRequest>"
					+ "</getPlaces>"
					+ "</soapenv:Body>" + "</soapenv:Envelope>";
			String soapAction = "http://nikmesoft.com/apis/SFoodServices/index.php/getPlaces";
			return xmlParserGetPlaces(Utilities.callWS(request, soapAction));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result==null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setTitle("Connect to network.");
				builder.setMessage("Error when connect to network. Please try again!");
				builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
				builder.show();
			} else {
				if (result.getClass().equals(ErrorCode.class)) {
				} else {
					if(totalPagePlaces>0)
					{
						for(int i = 1; i<= totalPagePlaces; i++){
							pagePlaces = i;
							WSLoaderGetPlaces ws = new WSLoaderGetPlaces();
							ws.execute();
						}
						
					}
					else {
						progressDialog.dismiss();
					}
				}
				
			}
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.setMessage("Reloading. Please wait...");
			progressDialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

	}

}
