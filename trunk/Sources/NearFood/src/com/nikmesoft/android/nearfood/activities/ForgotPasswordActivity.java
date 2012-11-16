package com.nikmesoft.android.nearfood.activities;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nikmesoft.android.nearfood.forgotpassword.*;
import com.nikmesoft.android.nearfood.R;

public class ForgotPasswordActivity extends BaseActivity {
	
	private EditText edtEmail;
	private Callforgot loar;
	private ProgressDialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		init();
	}

	private void init() {
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Loading...Please wait...");
	}
	
	public void onClickResetPassword(View v) {
	if("".equals(edtEmail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Please enter your email!", Toast.LENGTH_LONG).show();
		} else if(!checkValidateEmail(edtEmail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Wrong email address format!", Toast.LENGTH_LONG).show();
		} else {
			loar = new Callforgot();
			loar.execute(edtEmail.getText().toString().trim());
			/*Toast.makeText(getApplicationContext(), "Email the instructions to retrieve your password be sent to "+edtEmail.getText().toString().trim()+", please check your email for instructions to retrieve your password!", Toast.LENGTH_LONG).show();
			*/
			//setResult(RESULT_CANCELED);
			//finish();
		}
	}
	
	public void onClickBack(View v) {
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
	private class Callforgot extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return xmlParser(callWS(params[0]));
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@SuppressLint("NewApi")
		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
			if(result.equals("")){
				Toast.makeText(getApplicationContext(), "Email the instructions to retrieve your password be sent to "+edtEmail.getText().toString().trim()+", please check your email for instructions to retrieve your password!", Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
				dialog.dismiss();
			}
			else{
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				setResult(RESULT_CANCELED);
				dialog.dismiss();
			    }
			}
			catch(Exception e)
			{
				
			}
			
				
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		private String callWS(String name1) {
			String body = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
					"<soapenv:Header/>"
					+"<soapenv:Body>"
					+"<forgotPassword soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
					+"<ForgotPasswordRequest xsi:type=\"sfo:ForgotPasswordRequest\" xmlns:sfo=\"http://nikmesoft.com/apis/SFoodServices/\">"
					+"<email xsi:type=\"xsd:string\">"+name1+"</email>"
					+"</ForgotPasswordRequest>"
					+"</forgotPassword>"
					+"</soapenv:Body>"
					+"</soapenv:Envelope>";

			final DefaultHttpClient httpClient = new DefaultHttpClient();
			// request parameters
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 15000);
			// set parameter
			HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);

			// POST the envelope
			HttpPost httppost = new HttpPost("http://nikmesoft.com/apis/SFoodServices/index.php");
			// add headers
			httppost.setHeader("SoapAction",
					"http://nikmesoft.com/apis/SFoodServices/index.php/forgotPassword");
			httppost.setHeader("Content-Type", "text/xml; charset=utf-8");

			String responseString = "";
			try {

				// the entity holds the request
				HttpEntity entity = new StringEntity(body);
				httppost.setEntity(entity);

				// Response handler
				ResponseHandler<String> rh = new ResponseHandler<String>() {
					// invoked when client receives response
					public String handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {

						// get response entity
						HttpEntity entity = response.getEntity();

						// read the response as byte array
						StringBuffer out = new StringBuffer();
						byte[] b = EntityUtils.toByteArray(entity);

						// write the response byte array to a string buffer
						out.append(new String(b, 0, b.length));
						return out.toString();
					}
				};

				responseString = httpClient.execute(httppost, rh);

			} catch (Exception e) {
				Log.v("Exception", e.toString());
			}

			// close the connection
			httpClient.getConnectionManager().shutdown();
			Log.i("Webservice", responseString);
			return responseString;
		}

		private String xmlParser(String strXml) {
			byte xmlBytes[] = strXml.getBytes();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					xmlBytes);
			SAXParserFactory saxPF = SAXParserFactory.newInstance();
			SAXParser saxParser;
			try {
				saxParser = saxPF.newSAXParser();
				XMLReader xr = saxParser.getXMLReader();
				GetLoveHandler handler = new GetLoveHandler();
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
	}
}
