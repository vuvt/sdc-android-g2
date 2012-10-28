package com.nikmesoft.android.nearfood.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CropOptionAdapter;
import com.nikmesoft.android.nearfood.models.CropOption;
import com.nikmesoft.android.nearfood.utils.CommonUtil;

public class ProfileActivity extends BaseActivity {
	
	private EditText edtFullName, edtEmail, edtBirthDay;
	private RadioButton rbMale, rbFemale;
	private ImageView imageView;
	private String selectedImagePath;
	private Uri mImageCaptureUri;
	private Bitmap selectedAvatar;
	private float AVATAR_SIZE = 60;
	
	private int pYear;
	private int pMonth;
	private int pDay;
	
	private static final int CAPTURE_IMAGE = 100;
	private static final int SELECT_PICTURE = 101;
	private static final int CROP_PHOTO_CODE = 102;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		init();
		
		/** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	private void init() {
		edtFullName = (EditText) findViewById(R.id.edtFullName);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtBirthDay = (EditText) findViewById(R.id.edtBirthDay);
		rbMale = (RadioButton) findViewById(R.id.rbMale);
		rbFemale = (RadioButton) findViewById(R.id.rbFemale);
		imageView = (ImageView) findViewById(R.id.imgProfilePicture);
	}
	
	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void onClickSave(View v) {
		if(isNullAllFields()) {
			Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
		} else if(!checkValidateEmail(edtEmail.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Wrong email address format!", Toast.LENGTH_LONG).show();
		} else {
			//TODO ok ne
			Toast.makeText(getApplicationContext(), "edit ok!", Toast.LENGTH_LONG).show();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void onClickDatePicker(View v) {
		showDialog(0);
	}
	
	public void onClickTakeAPhoto(View v) {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAPTURE_IMAGE);
        
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageCaptureUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_avatar_"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg"));

		takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				mImageCaptureUri);
		takePictureIntent.putExtra("return-data", true);
		startActivityForResult(takePictureIntent, CAPTURE_IMAGE);
	}

	public void onClickChooseFromGallery(View v) {
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
        
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//		photoPickerIntent.setType("image/*");
//		startActivityForResult(photoPickerIntent, PICK_PHOTO_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
//		if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
//            imageView.setImageBitmap(photo);
//        } else if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_PICTURE) {
//                Uri selectedImageUri = data.getData();
//                selectedImagePath = getPath(selectedImageUri);
//                //System.out.println("Image Path : " + selectedImagePath);
//                imageView.setImageURI(selectedImageUri);
//            }
//        }
		
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CAPTURE_IMAGE:
				doCrop();
				break;
			case SELECT_PICTURE:
				mImageCaptureUri = data.getData();
				doCrop();
				break;
			case CROP_PHOTO_CODE:
				Bundle extras = data.getExtras();

				if (extras != null) {
					Bitmap bitmap = extras.getParcelable("data");
					imageView.setImageBitmap(bitmap);
					selectedAvatar = CommonUtil.getResizedBitmap(bitmap,
							(int) AVATAR_SIZE, (int) AVATAR_SIZE);
				}

				File f = new File(mImageCaptureUri.getPath());
				Log.d("PATH", mImageCaptureUri.getPath());
				if (f.exists()) {
					f.delete();
				}
				break;

			}

		}
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	

	/**
	 * check null all fields
	 * 
	 * @return true if have a field is null, invert return false if haven't
	 */
	public boolean isNullAllFields() {
		if ("".equals(edtFullName.getText().toString())
				|| "".equals(edtEmail.getText().toString())
				|| "".equals(edtBirthDay.getText().toString())
				|| (!rbMale.isChecked() && !rbFemale.isChecked())) {
			return true;
		}
		return false;
	}

	/**
	 * check validate email address format
	 * @param email_address
	 * @return true if valid, false if invalid
	 */
	public boolean checkValidateEmail(String email_address){
		return email_address.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	
	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();

		if (size == 0) {
			CommonUtil.toastNotify(this, getString(R.string.crop_image_no_app));
			return;
		} else {
			intent.setData(mImageCaptureUri);

			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CROP_PHOTO_CODE);
			} else {
				for (ResolveInfo res : list) {
					final CropOption cropoption = new CropOption();

					cropoption.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					cropoption.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					cropoption.appIntent = new Intent(intent);

					cropoption.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(cropoption);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(this,
						R.layout.cropoption_item, cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.crop_image_dialog_title));
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_PHOTO_CODE);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {

						if (mImageCaptureUri != null) {
							getContentResolver().delete(mImageCaptureUri, null,
									null);
							mImageCaptureUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}
	
	// =======date picker============
		/** Callback received when the user "picks" a date in the dialog */
		private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				pYear = year;
				pMonth = monthOfYear;
				pDay = dayOfMonth;
				updateDisplay();
			}
		};

		/** Updates the date in the TextView */
		private void updateDisplay() {
			edtBirthDay.setText(new StringBuilder()
					// Month is 0 based so add 1
					.append(pDay).append("/").append(pMonth + 1).append("/")
					.append(pYear));
		}

		/** Create a new dialog for date picker */
		@Override
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case 0:
				return new DatePickerDialog(this, pDateSetListener, pYear, pMonth,
						pDay);
			}
			return null;
		}
}
