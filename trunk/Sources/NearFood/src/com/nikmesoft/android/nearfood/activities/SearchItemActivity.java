package com.nikmesoft.android.nearfood.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.GalleryAdapter;
import com.nikmesoft.android.nearfood.adapters.SearchCheckInResultAdapter;
import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.Utilities;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchItemActivity extends Activity {
	Facebook facebook = new Facebook("302093569905424");
	private SharedPreferences mPrefs;
	private Button btnShare, btnShareOnFacebook, btnCancel;
	private TextView nameFacebook, namePlace, link, description, noteShare, search_name_place, address_information, description_information;
	private EditText contentShare;
	Dialog dialog_share;
	ProgressBar dialog_progressBar;
	private ProgressDialog mProgress;
	private ArrayList<Drawable> listImage;
	private ScrollView scrollview;
	private Gallery gallery;
	private ImageView left_arrow_imageview, right_arrow_imageview, imgFacebook, 
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

		search_name_place = (TextView) findViewById(R.id.search_name_place);
		address_information = (TextView) findViewById(R.id.address_information);
		description_information = (TextView) findViewById(R.id.description_information);
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

	}

	public void onClickLike(View v) {

	}

	public void onClickCheckInSearch(View v) {

	}

	public void onClickMap(View v) {
		/*SearchTabGroupActivity parent = (SearchTabGroupActivity)getParent();
		parent.startNewActivity(SearchMapActivity.class.getSimpleName(), new Intent(parent, SearchMapActivity.class));*/
		Intent intent = new Intent(this.getParent(), SearchMapActivity.class);
		startActivity(intent);
	}
	public void addItemListViewCustomer(ArrayList<CheckIn> checks) {
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
	public void onClickFacebook(View v) {
		dialog_share = new Dialog(this.getParent());
		dialog_share.setContentView(R.layout.share_face);
		/*LayoutInflater inflater = (LayoutInflater)getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.share_face, null);*/
		//dialog_share.setContentView(view);
		dialog_share.setTitle("Share facebook");
		noteShare = (TextView) dialog_share.findViewById(R.id.noteShare);
		dialog_progressBar =(ProgressBar) dialog_share.findViewById(R.id.dialogProgressBar);
		
		
		imgFacebook = (ImageView ) dialog_share.findViewById(R.id.imgFacebook);
		imgFacebook.setImageBitmap(Utilities.drawableToBitmap(listImage.get(0)));
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
    	namePlace = (TextView) dialog_share.findViewById(R.id.namePlace) ;
    	link = (TextView) dialog_share.findViewById(R.id.link);
    	description  = (TextView) dialog_share.findViewById(R.id.description);
    	btnShareOnFacebook = (Button) dialog_share.findViewById(R.id.btnShareOnFacebook);
    	btnCancel = (Button) dialog_share.findViewById(R.id.btnCancel);
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
    	dialog_share.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
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

			facebook.authorize(this.getParent(), new String[] {},-1, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					postPhotoWithMsg();
				}

				@Override
				public void onFacebookError(FacebookError error) {

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

				@Override
				public void onError(DialogError e) {

				}

				@Override
				public void onCancel() {Log.e("Facebook", "Error");
				SearchItemActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						dialog_progressBar.setVisibility(View.GONE);
						btnShare.setVisibility(View.GONE);
						btnShareOnFacebook.setVisibility(View.VISIBLE);
						btnCancel.setVisibility(View.VISIBLE);
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
			//data = Utilities.getBytes(bitmap);
			Bundle params = new Bundle();
			/*params.putString("message", msg);
			params.putByteArray("picture", data)*/;
			params.putString("message", contentShare.getText().toString());
			Log.d("Message", contentShare.getText().toString());
			params.putString("name", namePlace.getText().toString());
			params.putString("link", link.getText().toString());
			params.putString("description", description.getText().toString());
			params.putString("picture", "http://twitpic.com/show/thumb/6hqd44");
			
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
}
