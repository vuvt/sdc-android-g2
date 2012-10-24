package com.nikmesoft.android.nearfood.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.R;

public class RegisterActivity extends BaseActivity {

	private EditText edtFullName, edtEmail, edtPassword, edtConfirmPassword,
			edtBirthDay;
	private RadioGroup rgGender;
	private RadioButton rbMale, rbFemale;
	private Button btnRegister, btnReset;

	private int pYear;
	private int pMonth;
	private int pDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();

//		/** Get the current date */
//		final Calendar cal = Calendar.getInstance();
//		pYear = cal.get(Calendar.YEAR);
//		pMonth = cal.get(Calendar.MONTH);
//		pDay = cal.get(Calendar.DAY_OF_MONTH);
//
//		/** Display the current date in the TextView */
//		updateDisplay();
	}

	private void init() {
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
				if (isNullAllFields()) { //null all fields
					Toast.makeText(getApplicationContext(),
							"Please fill all fields!", Toast.LENGTH_LONG).show();
					
				} else if(!checkValidateEmail(edtEmail.getText().toString())) { //check email
					Toast.makeText(getApplicationContext(),
							"Wrong email address format!", Toast.LENGTH_LONG).show();
				} else if(!checkConfirmPassword(edtPassword.getText().toString(), edtConfirmPassword.getText().toString())) {
					Toast.makeText(getApplicationContext(),
							"Confirmpassword do not match!", Toast.LENGTH_LONG).show();
				} else { // ok ne`
					//TODO xu ly dang ky o day
					Toast.makeText(getApplicationContext(),
							"Register successfully!", Toast.LENGTH_LONG).show();
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
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * onClick for choose BirthDay button
	 * 
	 * @param v
	 */
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
	 * @return true if all fields is null, invert return false if not null
	 */
	public boolean isNullAllFields() {
		if (edtFullName.getText().equals(null)
				|| edtEmail.getText().equals(null)
				|| edtPassword.getText().equals(null)
				|| edtConfirmPassword.getText().equals(null)
				|| edtBirthDay.getText().equals(null)
				|| (!rbMale.isChecked() & !rbFemale.isChecked())) {
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
	
	/**
	 * check the same password with confirm password
	 * @param pass
	 * @param confirmPass
	 * @return true if equals, false if not same
	 */
	public boolean checkConfirmPassword(String pass, String confirmPass){
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
}
