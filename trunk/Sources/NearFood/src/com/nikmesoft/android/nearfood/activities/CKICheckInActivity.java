package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.nikmesoft.android.nearfood.handlers.CheckInHandler;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.HttpFileUploader;
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
	int id_checkin;
	ImageView img_checkin;
	EditText edt_description;
	Button bt_take_photo, bt_choose_image, bt_share_facebook;
	private int REQ_CODE_PICK_IMAGE = 100, REQ_CODE_TAKE_PHOTO = 101;
	Facebook facebook = new Facebook("302093569905424");
	private Bitmap photoShareOnFb = null;
	private SharedPreferences mPrefs;
	ProgressBar progressbar;
	ProgressDialog dialog;
	Dialog dialog_share;
	AddCheckInLoadder loader;
	private ImageView imgFacebook;
	private Button btnShare, btnShareOnFacebook, btnCancel;
	private TextView noteShare, namePlace, link, description;
	ProgressBar dialog_progressBar;
	private EditText contentShare;

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
		dialog = new ProgressDialog(CKICheckInActivity.this.getParent());
		dialog.setMessage("Loading...Please wait!");
		dialog.setCancelable(false);
	}

	public void actionChooseImage(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		getParent().startActivityForResult(
				Intent.createChooser(intent, "Select Picture"),
				REQ_CODE_PICK_IMAGE);
	}

	public void actionTakePhoto(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		getParent().startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			img_checkin.setImageBitmap(photo);
			photoShareOnFb = photo;
			bt_choose_image.setVisibility(View.INVISIBLE);
			bt_take_photo.setVisibility(View.INVISIBLE);
			return;
		} else if (requestCode == REQ_CODE_PICK_IMAGE
				&& resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
			img_checkin.setImageURI(Uri.parse(getPath(selectedImageUri)));
			img_checkin.buildDrawingCache();
			photoShareOnFb = img_checkin.getDrawingCache();
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
		if (checkConditionCheckIn()) {
			if(loader!=null&&!loader.isCancelled())
				loader.cancel(true);
			loader = new AddCheckInLoadder();
			loader.execute(
					/* String.valueOf(MyApplication.USER_CURRENT.getId()) */"44",
					place.getReferenceKey(),
					place.getName(),
					place.getPhoneNumber() == null ? "" : place
							.getPhoneNumber(),
					place.getAddress(),
					"",
					edt_description.getText().toString(),
					String.valueOf(place.getMapPoint().getLongitudeE6() / 1000000.0),
					String.valueOf(place.getMapPoint().getLatitudeE6() / 1000000.0));
		}
	}

	public void imgCheckInClick(View v) {
		if (bt_choose_image.getVisibility() == View.VISIBLE) {
			bt_choose_image.setVisibility(View.INVISIBLE);
			bt_take_photo.setVisibility(View.INVISIBLE);
		} else {
			bt_choose_image.setVisibility(View.VISIBLE);
			bt_take_photo.setVisibility(View.VISIBLE);
		}
		/*
		 * final ImgCheckInClickHandler handler = new ImgCheckInClickHandler();
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { for (int i = 0; i < 3; i++) {
		 * SystemClock.sleep(1000); } Bundle bundle = new Bundle();
		 * bundle.putBoolean("IS_END", true); Message msg = new Message();
		 * msg.setData(bundle); handler.handleMessage(msg); } }).start();
		 */
	}

	/*
	 * public class ImgCheckInClickHandler extends Handler { public void
	 * handleMessage(Message msg) { //if (msg.getData().getBoolean("IS_END",
	 * false)) { bt_choose_image.setVisibility(View.INVISIBLE);
	 * bt_take_photo.setVisibility(View.INVISIBLE); //} } }
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
			CheckInHandler handler = new CheckInHandler();
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

	private class AddCheckInLoadder extends AsyncTask<String, Integer, Object> {

		@Override
		protected Object doInBackground(String... params) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
					+ "<soapenv:Header/>\n"
					+ "\t<soapenv:Body>\n"
					+ "\t\t<addCheckIn soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
					+ "\t\t\t<AddCheckInRequest xsi:type=\"sfo:AddCheckInRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">\n"
					+ "<!--You may enter the following 9 items in any order-->\n"
					+ "\t\t\t\t<id_user xsi:type=\"xsd:int\">"
					+ params[0]
					+ "</id_user>\n"
					+ "\t\t\t\t<reference_key xsi:type=\"xsd:string\">"
					+ params[1]
					+ "</reference_key>\n"
					+ "\t\t\t\t<name xsi:type=\"xsd:string\">"
					+ params[2]
					+ "</name>\n"
					+ "\t\t\t\t<phone xsi:type=\"xsd:string\">"
					+ params[3]
					+ "</phone>\n"
					+ "\t\t\t\t<address xsi:type=\"xsd:string\">"
					+ params[4]
					+ "</address>\n"
					+ "\t\t\t\t<description xsi:type=\"xsd:string\">"
					+ params[5]
					+ "</description>\n"
					+ "\t\t\t\t<checkin_description xsi:type=\"xsd:string\">"
					+ params[6]
					+ "</checkin_description>\n"
					+ "\t\t\t\t<longitude xsi:type=\"xsd:double\">"
					+ params[7]
					+ "</longitude>\n"
					+ "\t\t\t\t<latitude xsi:type=\"xsd:double\">"
					+ params[8]
					+ "</latitude>\n"
					+ "\t\t\t</AddCheckInRequest>\n"
					+ "\t\t</addCheckIn>\n"
					+ "\t</soapenv:Body>\n"
					+ "</soapenv:Envelope>\n";
			Log.d("request", body);

			return xmlParser(Utilities
					.callWS(body,
							"http://nikmesoft.com/apis/SFoodServices/index.php/addCheckIn"));
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
				CommonUtil.dialogNotify(CKICheckInActivity.this.getParent(),
						((ErrorCode) result).getErrorMsg());
			} else if (result != null) {
				place.setId(((Integer[]) result)[0]);
				id_checkin = ((Integer[]) result)[1];
				dialog.dismiss();
				upload();
			} else {
				dialog.dismiss();
				CommonUtil.dialogNotify(CKICheckInActivity.this.getParent(),
						CKICheckInActivity.this.getResources().getString(R.string.title_connection_timeout));
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

	};

	// Upload image
	public void upload() {
		String url = "http://nikmesoft.com/apis/SFoodServices/checkInUploader.php";
		String filename = String.valueOf(id_checkin) + ".png";
		String s_data = "";
		Bitmap bm = ((BitmapDrawable) img_checkin.getDrawable()).getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, bos);
		byte[] data = bos.toByteArray();

		uploader.execute((Object) url, (Object) data, (Object) s_data,
				(Object) filename);
	}

	// Share facebook
	private AsyncTask<Object, Void, String> uploader = new AsyncTask<Object, Void, String>() {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(CKICheckInActivity.this.getParent());
			dialog.setMessage("Uploading...Please wait!");
			dialog.setCancelable(false);
			dialog.show();
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
			CKIGroupActivity parent = (CKIGroupActivity) getParent();
			Intent intent = new Intent(parent, CKIViewCheckInActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ExtraBinding.PLACE_BINDING, place);
			intent.putExtras(bundle);
			parent.startNewActivity(
					CKIViewCheckInActivity.class.getSimpleName(), intent);
		}

	};

	private boolean checkConditionCheckIn() {
		if (edt_description.getText().toString().trim().length() == 0) {
			Toast.makeText(this, "Please enter description!", Toast.LENGTH_LONG).show();
			return false;
		}
		if (photoShareOnFb==null){
			Toast.makeText(this, "Please set image!", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public void shareFacebook(View v) {
		dialog_share = new Dialog(this.getParent());
		dialog_share.setContentView(R.layout.share_face);
		dialog_share.setTitle("Share facebook");
		noteShare = (TextView) dialog_share.findViewById(R.id.noteShare);
		dialog_progressBar = (ProgressBar) dialog_share
				.findViewById(R.id.dialogProgressBar);

		imgFacebook = (ImageView) dialog_share.findViewById(R.id.imgFacebook);

		imgFacebook.setImageBitmap(photoShareOnFb);
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
		link.setText(place.getAddress());
		description = (TextView) dialog_share.findViewById(R.id.description);
		description.setText(edt_description.getText().toString().trim());
		btnShareOnFacebook = (Button) dialog_share
				.findViewById(R.id.btnShareOnFacebook);
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
		dialog_share
				.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
		dialog_share.show();
	}

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
							CKICheckInActivity.this
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
							CKICheckInActivity.this
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

			// byte[] data = null;
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

			AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
			mAsyncRunner.request("me/feed", params, "POST",
					new SampleUploadListener(), null);
		} else {
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
			CKICheckInActivity.this.runOnUiThread(new Runnable() {
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
