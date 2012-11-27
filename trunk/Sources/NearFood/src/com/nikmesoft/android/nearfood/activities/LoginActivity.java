package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.handlers.LoginHandler;
import com.nikmesoft.android.nearfood.models.User;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.Utilities;

public class LoginActivity extends BaseActivity {

	private EditText edtEmail, edtPassword;
	private ProgressDialog dialog;
	private WSLoader loader;
	
	private static final int REQUEST_REGISTER = 101;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...Please wait!");
		dialog.setCancelable(false);
	}
	
	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	public void onClickForgotPassword(View v) {
		Intent intent = new Intent(this, ForgotPasswordActivity.class);
		startActivity(intent);
	}
	
	public void onClickRegister(View v) {
//		Intent intent = new Intent(this, RegisterActivity.class);
//		startActivity(intent);
		
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		intent.putExtra("FromActivity", "Settings");
		startActivityForResult(intent, REQUEST_REGISTER);
	}
	
	public void onClickLogin(View v) {
		if("".equals(edtEmail.getText().toString().trim()) || "".equals(edtPassword.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
		} else if(!checkValidateEmail(edtEmail.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Wrong email address format!", Toast.LENGTH_LONG).show();
		} else {
			//TODO ok nef
			if (loader == null || loader.isCancelled()
					|| loader.getStatus() == Status.FINISHED) {
				loader = new WSLoader();
				loader.execute(edtEmail.getText().toString().trim(), CommonUtil.convertToMD5(edtPassword.getText().toString().trim()));
				
			}
		}
	}
	
	public void onClickCancel(View v) {
		//TODO vo main ma ko login
		setResult(RESULT_CANCELED);
		finish();
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
	 * goi WS
	 * @param name1
	 * @param name2
	 * @return
	 */
	
	/**
	 * phan tich xml WS tra ve
	 * @param strXml
	 * @return
	 */
	
	private Object xmlParser(String strXml) {
		if(strXml == null || strXml.length()==0) {
			ErrorCode err = new ErrorCode();
			err.setErrorID("errorID");
			err.setErrorMsg("Can't connect to server!");
			return err;
		}
		byte xmlBytes[] = strXml.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				xmlBytes);
		SAXParserFactory saxPF = SAXParserFactory.newInstance();
		SAXParser saxParser;
		try {
			saxParser = saxPF.newSAXParser();
			XMLReader xr = saxParser.getXMLReader();
			LoginHandler handler = new LoginHandler();
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
					"<login soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
					"<LoginRequest xsi:type=\"sfo:LoginRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">" +
					"<!--You may enter the following 2 items in any order-->" +
					"<email xsi:type=\"xsd:string\">" +
					params[0]+
					"</email>" +
					"<password xsi:type=\"xsd:string\">" +
					params[1] +
					"</password>" +
					"</LoginRequest>" +
					"</login>" +
					"</soapenv:Body>" +
					"</soapenv:Envelope>";
			return xmlParser(Utilities.callWS(body, "http://nikmesoft.com/apis/SFoodServices/index.php/login"));
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(result!=null&&result.getClass().equals(ErrorCode.class)){
				
				CommonUtil.dialogNotify(LoginActivity.this, ((ErrorCode)result).getErrorMsg());
				
			}else if(result!=null&&result.getClass().equals(User.class)){
				MyApplication.USER_CURRENT = new User();
				MyApplication.USER_CURRENT = (User)result;
				MyApplication.USER_CURRENT.setPassword(CommonUtil.convertToMD5(edtPassword.getText().toString().trim()));
				
				Toast.makeText(getApplicationContext(), "Login successfully!", Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
				finish();
				
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
}
