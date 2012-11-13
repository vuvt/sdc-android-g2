package com.nikmesoft.android.nearfood.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utilities {

	public static Drawable LoadImageFromWebOperations(String url, String srcName)
			throws MalformedURLException, IOException {
		InputStream is = (InputStream) new URL(url).getContent();
		Drawable d = Drawable.createFromStream(is, srcName);
		return d;

	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	public static String callWS(String request, String soapAction) {

		final DefaultHttpClient httpClient = new DefaultHttpClient();
		// request parameters
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		// set parameter
		HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);

		// POST the envelope
		HttpPost httppost = new HttpPost(
				"http://nikmesoft.com/apis/SFoodServices/index.php");
		// add headers
		httppost.setHeader("SoapAction", soapAction);
		httppost.setHeader("Content-Type", "text/xml; charset=utf-8");

		String responseString = "";
		try {

			// the entity holds the request
			HttpEntity entity = new StringEntity(request);
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

			System.out.println(responseString);

		} catch (Exception e) {
			Log.v("Exception", e.toString());
		}

		// close the connection
		httpClient.getConnectionManager().shutdown();
		Log.i("Webservice", responseString);
		return responseString;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}
