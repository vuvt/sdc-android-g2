package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
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

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CropOptionAdapter;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.ProfileHandler;
import com.nikmesoft.android.nearfood.models.CropOption;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.HttpFileUploader;
import com.nikmesoft.android.nearfood.utils.Utilities;

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
	
	private ProgressDialog dialog;
	private WSLoader loader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		init();
		
        
        //set DatePickerDialog
		setDateToDatePickerDialog(edtBirthDay.getText().toString().trim());
		
	}

	private void init() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...Please wait!");
		dialog.setCancelable(false);
		
		edtFullName = (EditText) findViewById(R.id.edtFullName);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtBirthDay = (EditText) findViewById(R.id.edtBirthDay);
		rbMale = (RadioButton) findViewById(R.id.rbMale);
		rbFemale = (RadioButton) findViewById(R.id.rbFemale);
		imageView = (ImageView) findViewById(R.id.imgProfilePicture);
		
		//set information to each field
		edtFullName.setText(MyApplication.USER_CURRENT.getFullName());
		edtEmail.setText(MyApplication.USER_CURRENT.getEmail());
		edtBirthDay.setText(MyApplication.USER_CURRENT.getBirthday());
		if(MyApplication.USER_CURRENT.getGender().equals("1")) {
			rbMale.setChecked(true);
		} else {
			rbFemale.setChecked(true);
		}
		Log.d("IMGGGGGGGGG", MyApplication.USER_CURRENT.getProfilePicture());
		
		loadAvatar.execute(MyApplication.USER_CURRENT.getProfilePicture());
	}
	
	private AsyncTask<String, Void, Drawable> loadAvatar = new AsyncTask<String, Void, Drawable>() {

		@Override
		protected Drawable doInBackground(String... params) {
			Drawable dr = null;
			try {
				dr =  Utilities.LoadImageFromWebOperations(params[0], "Avatar");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return dr;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Drawable result) {
			imageView.setImageDrawable(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	};
	
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
			if (loader == null || loader.isCancelled()
					|| loader.getStatus() == Status.FINISHED) {
				loader = new WSLoader();
				loader.execute(
						String.valueOf(MyApplication.USER_CURRENT.getId()),
						edtFullName.getText().toString().trim(),
						 edtBirthDay.getText().toString().trim(),
						rbMale.isChecked() ? "1" : "0",
						"108.149665",
						"16.074641");

			}
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
        
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
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
		if ("".equals(edtFullName.getText().toString().trim())
				|| "".equals(edtEmail.getText().toString().trim())
				|| "".equals(edtBirthDay.getText().toString().trim())
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
		
		private void setDateToDatePickerDialog(String dateString) {
			
			DateFormat parseFormat = new SimpleDateFormat("dd/MM/yy");
		    DateFormat formattingFormat = new SimpleDateFormat("dd/MM/yyyy");
		    
		    String date;
			try {
				date = formattingFormat.format(parseFormat.parse(dateString));
				pDay = Integer.parseInt(date.substring(0, 2));
				pMonth = Integer.parseInt(date.substring(3, 5))-1;
				pYear = Integer.parseInt(date.substring(6, 10));
				
				Log.d("DATE", date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		//=============call WS============
		
		/**
		 * phan tich xml WS tra ve
		 * @param strXml
		 * @return
		 */
		
		private Object xmlParser(String strXml) {
			byte xmlBytes[] = strXml.getBytes();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					xmlBytes);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxParser;
			try {
				saxParser = saxPF.newSAXParser();
				XMLReader xr = saxParser.getXMLReader();
				ProfileHandler handler = new ProfileHandler();
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
		
		private class WSLoader extends AsyncTask<String, Integer, Object> {

			@Override
			protected Object doInBackground(String... params) {
				String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
						"<soapenv:Header/>" +
						"<soapenv:Body>" +
						"<updateProfile soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
						"<UpdateProfileRequest xsi:type=\"sfo:UpdateProfileRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">" +
						"<!--You may enter the following 6 items in any order-->" +
						"<id xsi:type=\"xsd:int\">" +
						params[0] +
						"</id>" +
						"<fullname xsi:type=\"xsd:string\">" +
						params[1] +
						"</fullname>" +
						"<birthday xsi:type=\"xsd:string\">" +
						params[2] +
						"</birthday>" +
						"<gender xsi:type=\"xsd:int\">" +
						params[3] +
						"</gender>" +
						"<longitude xsi:type=\"xsd:double\">" +
						params[4] +
						"</longitude>" +
						"<latitude xsi:type=\"xsd:double\">" +
						params[5] +
						"</latitude>" +
						"</UpdateProfileRequest>" +
						"</updateProfile>" +
						"</soapenv:Body>" +
						"</soapenv:Envelope>";
				return xmlParser(Utilities.callWS(body, "http://nikmesoft.com/apis/SFoodServices/index.php/updateProfile"));
			}

			@Override
			protected void onCancelled() {
				super.onCancelled();
			}

			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				
				if(result!=null&&result.getClass().equals(ErrorCode.class)){
					//truong hop co loi
					dialog.dismiss();
					CommonUtil.dialogNotify(ProfileActivity.this, ((ErrorCode)result).getErrorMsg());
				}else if(result!=null) { //ko loi
					//MyApplication.USER_CURRENT = new User();
					//MyApplication.USER_CURRENT = (User)result;
					MyApplication.USER_CURRENT.setFullName(edtFullName.getText().toString().trim());
					MyApplication.USER_CURRENT.setBirthday(edtBirthDay.getText().toString().trim());
					MyApplication.USER_CURRENT.setGender(rbMale.isChecked()?"1":"0");
					
					uploadAvatarToServer();
					
				}
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				dialog.show();
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
			}

		}
		
		// ============upload avatar to server==============

		public void uploadAvatarToServer() {
			String url = "http://nikmesoft.com/apis/SFoodServices/avatarUploader.php";
			String filename = String.valueOf(MyApplication.USER_CURRENT.getId()) + ".png";
			Log.d("IDIDIDIDIDI", filename);
			String s_data = "";
			Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();

			uploader.execute((Object) url, (Object) data, (Object) s_data,
					(Object) filename);
		}

		private AsyncTask<Object, Void, String> uploader = new AsyncTask<Object, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected String doInBackground(Object... params) {
				String url = (String) params[0];
				byte[] data = (byte[]) params[1];
				String s_data = (String) params[2];
				String fileName = (String) params[3];
				HttpFileUploader fileUploader = new HttpFileUploader(url, fileName,
						data, s_data);
				return fileUploader.doUpload();
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				dialog.dismiss();

				if (!result.equals("")) { //have error
					CommonUtil.dialogNotify(ProfileActivity.this, result);
				} else {
					Toast.makeText(getApplicationContext(), "Update profile successfully!", Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
//					startActivity(intent);
					setResult(RESULT_CANCELED);
					finish();
				}

			}

		};
}
