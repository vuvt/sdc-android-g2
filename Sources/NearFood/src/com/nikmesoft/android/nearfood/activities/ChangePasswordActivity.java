package com.nikmesoft.android.nearfood.activities;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.R;
import com.nikmesoft.android.nearfood.handlers.ChangePasswordHandler;
import com.nikmesoft.android.nearfood.handlers.ErrorCode;
import com.nikmesoft.android.nearfood.utils.CommonUtil;
import com.nikmesoft.android.nearfood.utils.Utilities;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends BaseActivity {

	private EditText new_pass,old_pass,conf_pass;
	private ProgressDialog dialog;
	private WSLoader loader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		init();
	}
	private void init(){
		dialog = new ProgressDialog(getParent());
		dialog.setMessage("Loading...Please wait!");
		dialog.setCancelable(false);
		
		new_pass=(EditText) findViewById(R.id.edtNew_Password);
		old_pass=(EditText) findViewById(R.id.edtOld_Password);
		conf_pass=(EditText) findViewById(R.id.edtConf_Password);
	}
	public void onClickSave(View v){
		if(new_pass.getText().toString().length()==0 || old_pass.getText().toString().length()==0 || conf_pass.getText().toString().length()==0)
			Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
		else 
		   if(!MyApplication.USER_CURRENT.getPassword().equals(CommonUtil.convertToMD5(old_pass.getText().toString())))
			   Toast.makeText(getApplicationContext(), "Wrong old password!", Toast.LENGTH_LONG).show();
		   else
			   if(!conf_pass.getText().toString().equals(new_pass.getText().toString()))
				   Toast.makeText(getApplicationContext(), "Please check confirm password!", Toast.LENGTH_LONG).show();
			   else 
				   if (loader == null || loader.isCancelled()
							|| loader.getStatus() == Status.FINISHED) {
						loader = new WSLoader();
						loader.execute(String.valueOf(MyApplication.USER_CURRENT.getId()),CommonUtil.convertToMD5(new_pass.getText().toString().trim()));
			   		}
	}
	
	public void onClickBack(View v) {
		setResult(RESULT_CANCELED);
		finish();
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
					ChangePasswordHandler handler = new ChangePasswordHandler();
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
							"<changePassword soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">" +
							"<ChangePasswordRequest xsi:type=\"sfo:ChangePasswordRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">" +
							"<!--You may enter the following 2 items in any order-->" +
							"<id xsi:type=\"xsd:int\">" +
							Integer.parseInt(params[0]) +
							"</id>" +
							"<newpassword xsi:type=\"xsd:string\">" +
							params[1] +
							"</newpassword>" +
							"</ChangePasswordRequest>" +
							"</changePassword>" +
							"</soapenv:Body>" +
							"</soapenv:Envelope>";
					return xmlParser(Utilities.callWS(body, "http://nikmesoft.com/apis/SFoodServices/index.php/changePassword"));
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
						//truong hop co loi
						CommonUtil.dialogNotify(ChangePasswordActivity.this, ((ErrorCode)result).getErrorMsg());
					}else if(result!=null) { //ko loi
						MyApplication.USER_CURRENT.setPassword(CommonUtil.convertToMD5(new_pass.getText().toString().trim()));
						
						Toast.makeText(getApplicationContext(), "Change password successfully!", Toast.LENGTH_LONG).show();
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