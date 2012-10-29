package com.nikmesoft.android.nearfood.activities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.binding.ExtraBinding;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.Utilities;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

@SuppressWarnings("deprecation")
public class CKICheckInActivity extends BaseActivity {

	Place place;
	ImageView img_checkin;
	EditText edt_description;
	Button bt_take_photo, bt_choose_image, bt_share_facebook;
	private boolean hasImage = false;
	private int REQ_CODE_PICK_IMAGE = 100, REQ_CODE_TAKE_PHOTO = 101;
	Facebook facebook = new Facebook("204816289643968");
	private Bitmap photoShareOnFb = null;
	private SharedPreferences mPrefs;
	ProgressBar progressbar;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_checkin);
		init();
	}

	private void init() {
		this.place = (Place) getIntent().getExtras().get("place");

		TextView tv_placename = (TextView) findViewById(R.id.tv_place_name);
		tv_placename.setText(place.getName());

		TextView tv_placeaddress = (TextView) findViewById(R.id.tv_place_address);
		tv_placeaddress.setText(place.getAddress());
		// img_checkin=(ImageView)findViewById(R.id.img_checkin);
		edt_description = (EditText) findViewById(R.id.edt_description);
		img_checkin = (ImageView) findViewById(R.id.img_checkin);
		bt_choose_image = (Button) findViewById(R.id.bt_choose_img);
		bt_take_photo = (Button) findViewById(R.id.bt_take_photo);
		bt_share_facebook = (Button) findViewById(R.id.bt_share_facebook);
		progressbar = (ProgressBar) findViewById(R.id.progressbar);
	}

	public void actionChooseImage(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				REQ_CODE_PICK_IMAGE);
	}

	public void actionTakePhoto(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			img_checkin.setImageBitmap(photo);
			photoShareOnFb = photo;
			hasImage = true;
			bt_choose_image.setVisibility(View.INVISIBLE);
			bt_take_photo.setVisibility(View.INVISIBLE);
		} else if (requestCode == REQ_CODE_PICK_IMAGE
				&& resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
			// System.out.println("Image Path : " + selectedImagePath);

			try {
				Bitmap photo = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(),
						Uri.parse(getPath(selectedImageUri)));
				img_checkin.setImageBitmap(photo);
				photoShareOnFb = photo;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hasImage = true;
			bt_choose_image.setVisibility(View.INVISIBLE);
			bt_take_photo.setVisibility(View.INVISIBLE);
		} else
			facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void checkIn(View v) {
		startCheckIn();
		CKIGroupActivity parent = (CKIGroupActivity) getParent();
		Intent intent = new Intent(parent, CKIViewCheckInActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(ExtraBinding.PLACE_BINDING, place);
		intent.putExtras(bundle);
		parent.startNewActivity(CKIViewCheckInActivity.class.getSimpleName(),
				intent);
	}

	public void startCheckIn() {

	}

	public void imgCheckInClick(View v) {
		bt_choose_image.setVisibility(View.VISIBLE);
		bt_take_photo.setVisibility(View.VISIBLE);
	}

	public void shareFacebook(View v) {
		if (checkImage()) {
			bt_share_facebook.setVisibility(View.GONE);
			edt_description.setVisibility(View.GONE);
			bt_choose_image.setVisibility(View.GONE);
			bt_take_photo.setVisibility(View.GONE);
			progressbar.setVisibility(View.VISIBLE);
			shareOnFacebook(photoShareOnFb, edt_description.getText()
					.toString());
		}
	}

	public void shareOnFacebook(final Bitmap photo, final String msg) {
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

			facebook.authorize(this, new String[] {}, new DialogListener() {
				public void onComplete(Bundle values) {
					SharedPreferences.Editor editor = mPrefs.edit();
					editor.putString("access_token", facebook.getAccessToken());
					editor.putLong("access_expires",
							facebook.getAccessExpires());
					editor.commit();
					postPhotoWithMsg(photo, msg);
				}

				public void onFacebookError(FacebookError error) {

					Log.e("Facebook", "Error");
					CKICheckInActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							progressbar.setVisibility(View.GONE);
							CommonUtil.dialogNotify(getParent().getParent(),
									"Please verify Network Connection!");
							bt_share_facebook.setVisibility(View.VISIBLE);
							edt_description.setVisibility(View.VISIBLE);
							bt_choose_image.setVisibility(View.VISIBLE);
							bt_take_photo.setVisibility(View.VISIBLE);
						}
					});

				}

				public void onError(DialogError e) {

				}

				public void onCancel() {
					Log.e("Facebook", "Error");
					CKICheckInActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							progressbar.setVisibility(View.GONE);
							CommonUtil.dialogNotify(getParent().getParent(),
									"Please verify Network Connection!");
							bt_share_facebook.setVisibility(View.VISIBLE);
							edt_description.setVisibility(View.VISIBLE);
							bt_choose_image.setVisibility(View.VISIBLE);
							bt_take_photo.setVisibility(View.VISIBLE);
						}
					});

				}
			});
		} else {
			postPhotoWithMsg(photo, msg);
		}

	}

	public void postPhotoWithMsg(Bitmap bitmap, String msg) {
		if (Utilities.isNetworkAvailable(getApplicationContext())) {
			byte[] data = null;
			data = Utilities.getBytes(bitmap);
			Bundle params = new Bundle();
			params.putString("message", edt_description.getText().toString());
			params.putByteArray("picture", data);
			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
			mAsyncRunner.request("me/photos", params, "POST",
					new SampleUploadListener(), null);
		} else {
			progressbar.setVisibility(View.GONE);
			CommonUtil.dialogNotify(getParent().getParent(),
					"Please verify Network Connection!");
			bt_share_facebook.setVisibility(View.VISIBLE);
			edt_description.setVisibility(View.VISIBLE);
			bt_choose_image.setVisibility(View.VISIBLE);
			bt_take_photo.setVisibility(View.VISIBLE);
		}

	}

	private boolean checkImage() {
		if (!hasImage)
			bt_share_facebook.setVisibility(View.GONE);
			edt_description.setVisibility(View.GONE);
			bt_choose_image.setVisibility(View.GONE);
			bt_take_photo.setVisibility(View.GONE);
			Toast.makeText(this, "Please choose image!", Toast.LENGTH_LONG)
					.show();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bt_share_facebook.setVisibility(View.VISIBLE);
			edt_description.setVisibility(View.VISIBLE);
			bt_choose_image.setVisibility(View.VISIBLE);
			bt_take_photo.setVisibility(View.VISIBLE);
		return hasImage;

	}

	public class SampleUploadListener extends BaseKeyListener implements
			RequestListener {

		public void onComplete(final String response, final Object state) {
			Log.d("Facebook", "Response: " + response.toString());
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressbar.setVisibility(View.GONE);
					CommonUtil.dialogNotify(getParent().getParent(),
							"Please verify Network Connection!");
					bt_share_facebook.setVisibility(View.VISIBLE);
					edt_description.setVisibility(View.VISIBLE);
					bt_choose_image.setVisibility(View.VISIBLE);
					bt_take_photo.setVisibility(View.VISIBLE);
				}
			});

		}

		public void onFacebookError(FacebookError e, Object state) {

			Log.e("Facebook", "Error");
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressbar.setVisibility(View.GONE);
					CommonUtil.dialogNotify(getParent().getParent(),
							"Please verify Network Connection!");
					bt_share_facebook.setVisibility(View.VISIBLE);
					edt_description.setVisibility(View.VISIBLE);
					bt_choose_image.setVisibility(View.VISIBLE);
					bt_take_photo.setVisibility(View.VISIBLE);
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressbar.setVisibility(View.GONE);
					CommonUtil.dialogNotify(getParent().getParent(),
							"Please verify Network Connection!");
					bt_share_facebook.setVisibility(View.VISIBLE);
					edt_description.setVisibility(View.VISIBLE);
					bt_choose_image.setVisibility(View.VISIBLE);
					bt_take_photo.setVisibility(View.VISIBLE);
				}
			});
		}

		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			Log.e("Facebook", "Error");
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressbar.setVisibility(View.GONE);
					CommonUtil.dialogNotify(getParent().getParent(),
							"Please verify Network Connection!");
					bt_share_facebook.setVisibility(View.VISIBLE);
					edt_description.setVisibility(View.VISIBLE);
					bt_choose_image.setVisibility(View.VISIBLE);
					bt_take_photo.setVisibility(View.VISIBLE);
				}
			});
		}

		public void onMalformedURLException(MalformedURLException e,
				Object state) {

			Log.e("Facebook", "Error");
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressbar.setVisibility(View.GONE);
					CommonUtil.dialogNotify(getParent().getParent(),
							"Please verify Network Connection!");
					bt_share_facebook.setVisibility(View.VISIBLE);
					edt_description.setVisibility(View.VISIBLE);
					bt_choose_image.setVisibility(View.VISIBLE);
					bt_take_photo.setVisibility(View.VISIBLE);
				}
			});
		}
	}

	// private void share(String nameApp, String imagePath) {
	// List<Intent> targetedShareIntents = new ArrayList<Intent>();
	// Intent share = new Intent(android.content.Intent.ACTION_SEND);
	// share.setType("image/jpeg");
	// List<ResolveInfo> resInfo =
	// getPackageManager().queryIntentActivities(share, 0);
	// if (!resInfo.isEmpty()){
	// for (ResolveInfo info : resInfo) {
	// Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
	// targetedShare.setType("image/jpeg"); // put here your mime type
	//
	//
	// if (info.activityInfo.packageName.toLowerCase().contains(nameApp) ||
	// info.activityInfo.name.toLowerCase().contains(nameApp)) {
	// targetedShare.putExtra(Intent.EXTRA_TEXT,
	// edt_description.getText().toString()+"/email");
	// targetedShare.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new
	// File(imagePath)) );
	// targetedShare.setPackage(info.activityInfo.packageName);
	// targetedShareIntents.add(targetedShare);
	// }
	// }
	//
	// Intent chooserIntent =
	// Intent.createChooser(targetedShareIntents.remove(0),
	// "Select app to share");
	// chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
	// targetedShareIntents.toArray(new Parcelable[]{}));
	// startActivity(chooserIntent);
	// }
	// }
}
