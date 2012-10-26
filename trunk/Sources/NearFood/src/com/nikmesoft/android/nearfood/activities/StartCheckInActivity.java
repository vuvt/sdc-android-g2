package com.nikmesoft.android.nearfood.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.models.Place;

public class StartCheckInActivity extends BaseActivity {

	Place place;
	ImageView img_checkin;
	EditText edt_description;
	private boolean hasImage=false;
	private int REQ_CODE_PICK_IMAGE = 100, REQ_CODE_TAKE_PHOTO = 101;

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
		img_checkin = (ImageView) findViewById(R.id.img_checked);
	}

	public void actionChooseImage(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
	}

	public void actionTakePhoto(View v) {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQ_CODE_PICK_IMAGE) {
			if (resultCode == Activity.RESULT_OK && data != null) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();

				img_checkin.setImageBitmap(BitmapFactory
						.decodeFile(picturePath));
				hasImage=true;
			}
		} else if (requestCode == REQ_CODE_TAKE_PHOTO) {
			if (data != null) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");
				img_checkin.setImageBitmap(photo);
				hasImage=true;
			} 
		}
	}
	public void shareFacebook(View v){
		if(checkImage()){
			
		}
	}
	public void shareTwitter(View v){
		if(checkImage()){
			
		}
	}
	private boolean checkImage(){
		if(!hasImage)
			Toast.makeText(this, "Please choose image!", Toast.LENGTH_LONG).show();
		return hasImage;
			
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
