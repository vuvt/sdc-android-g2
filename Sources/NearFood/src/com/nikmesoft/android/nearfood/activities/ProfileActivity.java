package com.nikmesoft.android.nearfood.activities;

import com.nikmesoft.android.nearfood.R;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity {
	
	private EditText edtFullName, edtEmail, edtBirthDay;
	private RadioButton rbMale, rbFemale;
	
	private int pYear;
	private int pMonth;
	private int pDay;

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
