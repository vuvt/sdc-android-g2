package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.adapters.CropOptionAdapter;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.RegisterHandler;
import com.nikmesoft.android.nearfood.models.CropOption;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.HttpFileUploader;
import com.nikmesoft.android.nearfood.utils.Utilities;

public class RegisterActivity extends BaseActivity {

	private EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword,
			edtBirthDay;
	private RadioGroup rgGender;
	private RadioButton rbMale, rbFemale;
	private Button btnRegister, btnReset;
	private ImageView imageView;
	private ProgressDialog dialog;
	private WSLoader loader;
	private User user_registed;

	private int pYear;
	private int pMonth;
	private int pDay;
	
	private Uri mImageCaptureUri;
	private Bitmap selectedAvatar;
	private float AVATAR_SIZE = 60;
	
	private static final int CAPTURE_IMAGE = 100;
	private static final int SELECT_PICTURE = 101;
	private static final int CROP_PHOTO_CODE = 102;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();

		/* Get the current date */
		final Calendar cal = Calendar.getInstance();
		pYear = cal.get(Calendar.YEAR);
		pMonth = cal.get(Calendar.MONTH);
		pDay = cal.get(Calendar.DAY_OF_MONTH);
	}

	private void init() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...Please wait!");
		dialog.setCancelable(false);

		imageView = (ImageView) findViewById(R.id.imgProfilePicture);
		edtFullName = (EditText) findViewById(R.id.edtFullName);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
		edtBirthDay = (EditText) findViewById(R.id.edtBirthDay);
		rgGender = (RadioGroup) findViewById(R.id.rgGender);
		rbMale = (RadioButton) findViewById(R.id.rbMale);
		rbFemale = (RadioButton) findViewById(R.id.rbFemale);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isNullAllFields()) { // null all fields
					Toast.makeText(getApplicationContext(),
							"Please fill all fields!", Toast.LENGTH_LONG)
							.show();

				} else if (!checkValidateEmail(edtEmail.getText().toString())) { // check
																					// email
					Toast.makeText(getApplicationContext(),
							"Wrong email address format!", Toast.LENGTH_LONG)
							.show();
				} else if (!checkConfirmPassword(edtPassword.getText()
						.toString(), edtConfirmPassword.getText().toString())) {
					Toast.makeText(getApplicationContext(),
							"Confirmpassword do not match!", Toast.LENGTH_LONG)
							.show();
				} else { // ok ne`
					// TODO xu ly dang ky o day

					if (loader == null || loader.isCancelled()
							|| loader.getStatus() == Status.FINISHED) {
						loader = new WSLoader();
						loader.execute(
								edtFullName.getText().toString().trim(),
								edtEmail.getText().toString().trim(),
								CommonUtil.convertToMD5(edtPassword.getText()
										.toString().trim()), edtBirthDay
										.getText().toString().trim(),
								rbMale.isChecked() ? "1" : "0", "108.149665",
								"16.074641");

					}
				}
			}
		});
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				edtFullName.setText(null);
				edtEmail.setText(null);
				edtPassword.setText(null);
				edtConfirmPassword.setText(null);
				edtBirthDay.setText(null);
				rgGender.clearCheck();
			}
		});
	}

	public void onClickBack(View v) {
		onBackPressed();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Yes button clicked
				setResult(RESULT_CANCELED);
				finish();
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				break;
			}
		}
	};

	@Override
	public void onBackPressed() {
		if (!"".equals(edtFullName.getText().toString())
				|| !"".equals(edtEmail.getText().toString())
				|| !"".equals(edtPassword.getText().toString())
				|| !"".equals(edtConfirmPassword.getText().toString())
				|| !"".equals(edtBirthDay.getText().toString())
				|| (rbMale.isChecked() || rbFemale.isChecked())) { // not null
																	// all
																	// fields
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure want to cancel the register?")
					.setPositiveButton("Yes", dialogClickListener)
					.setNegativeButton("No", dialogClickListener).show();
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	/**
	 * onClick for choose BirthDay button
	 * 
	 * @param v
	 */
	@SuppressWarnings("deprecation")
	public void onClickDatePicker(View v) {
		// DatePickerFragment newFragment = new DatePickerFragment();
		// newFragment.show(getFragmentManager(), "datePicker");
		// edtBirthDay.setText(DatePickerFragment.date);
		// Toast.makeText(getApplicationContext(), newFragment.getDate(),
		// Toast.LENGTH_LONG).show();
		showDialog(0);
	}

	/**
	 * check null all fields
	 * 
	 * @return true if have a field is null, invert return false if haven't
	 */
	public boolean isNullAllFields() {
		if ("".equals(edtFullName.getText().toString())
				|| "".equals(edtEmail.getText().toString())
				|| "".equals(edtPassword.getText().toString())
				|| "".equals(edtConfirmPassword.getText().toString())
				|| "".equals(edtBirthDay.getText().toString())
				|| (!rbMale.isChecked() && !rbFemale.isChecked())) {
			return true;
		}
		return false;
	}

	/**
	 * check validate email address format
	 * 
	 * @param email_address
	 * @return true if valid, false if invalid
	 */
	public boolean checkValidateEmail(String email_address) {
		return email_address.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}

	/**
	 * check the same password with confirm password
	 * 
	 * @param pass
	 * @param confirmPass
	 * @return true if equals, false if not same
	 */
	public boolean checkConfirmPassword(String pass, String confirmPass) {
		return pass.equals(confirmPass);
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
				.append(pYear).append(" "));
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

	// =========call WS =================

	/**
	 * phan tich xml WS tra ve
	 * 
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
			RegisterHandler handler = new RegisterHandler();
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

	/**
	 * 
	 * @author thaivu
	 * 
	 */
	private class WSLoader extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
					+ "<soapenv:Header/>"
					+ "<soapenv:Body>"
					+ "<register soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+ "<RegisterRequest xsi:type=\"sfo:RegisterRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+ "<fullname xsi:type=\"xsd:string\">"
					+ params[0]
					+ "</fullname>"
					+ "<email xsi:type=\"xsd:string\">"
					+ params[1]
					+ "</email>"
					+ "<password xsi:type=\"xsd:string\">"
					+ params[2]
					+ "</password>"
					+ "<birthday xsi:type=\"xsd:string\">"
					+ params[3]
					+ "</birthday>"
					+ "<gender xsi:type=\"xsd:int\">"
					+ params[4]
					+ "</gender>"
					+ "<longitude xsi:type=\"xsd:double\">"
					+ params[5]
					+ "</longitude>"
					+ "<latitude xsi:type=\"xsd:double\">"
					+ params[6]
					+ "</latitude>"
					+ "</RegisterRequest>"
					+ "</register>" + "</soapenv:Body>" + "</soapenv:Envelope>";
			return xmlParser(Utilities
					.callWS(body,
							"http://nikmesoft.com/apis/SFoodServices/index.php/register"));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			if (result != null && result.getClass().equals(ErrorCode.class)) {
				dialog.dismiss();
				CommonUtil.dialogNotify(RegisterActivity.this,
						((ErrorCode) result).getErrorMsg());
			} else if (result != null && result.getClass().equals(User.class)) {
				user_registed = new User();
				user_registed = (User) result;
				
				//upload avatar to server				
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
	
	//===================avatar===================
	
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
	
	
	//============upload avatar to server==============
	
	public void uploadAvatarToServer() {
		String url = "http://nikmesoft.com/apis/SFoodServices/avatarUploader.php";
		String filename = String.valueOf(user_registed.getId())+".png";
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
			
			Toast.makeText(getApplicationContext(), "Register successfully!",
					Toast.LENGTH_LONG).show();
			Intent intent = new Intent(RegisterActivity.this,
					LoginActivity.class);
			startActivity(intent);
		}

	};
}
