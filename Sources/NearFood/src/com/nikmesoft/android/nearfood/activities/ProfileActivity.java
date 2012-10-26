package com.nikmesoft.android.nearfood.activities;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;

public class ProfileActivity extends BaseActivity {
	
	private EditText edtFullName, edtEmail, edtBirthDay;
	private RadioButton rbMale, rbFemale;
	private ImageView imageView;
	private String selectedImagePath;
	
	private int pYear;
	private int pMonth;
	private int pDay;
	
	private static final int CAPTURE_IMAGE = 100;
	private static final int SELECT_IMAGE = 101;
	private static final int SELECT_PICTURE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		init();
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
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
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
		
		if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        } else if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                //System.out.println("Image Path : " + selectedImagePath);
                imageView.setImageURI(selectedImageUri);
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
}
