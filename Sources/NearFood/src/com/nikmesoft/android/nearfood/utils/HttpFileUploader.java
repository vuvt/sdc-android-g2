package com.nikmesoft.android.nearfood.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpFileUploader {

	private URL connectURL;
	private String fileName = null;
	private byte[] data;
	private String s_data = null;

	public HttpFileUploader(String urlString, String fileName, byte[] data,
			String s_data) {
		try {
			connectURL = new URL(urlString);
		} catch (Exception ex) {

		}
		this.fileName = fileName;
		this.data = data;
		this.s_data = s_data;
	}

	public String doUpload() {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesAvailable, bufferSize;
		int maxBufferSize = 1 * 1024 * 1024;

		try {

			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) connectURL.openConnection();
			conn.setReadTimeout(30*1000);
			conn.setUseCaches(true);
			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + s_data
					+ "\"" + lineEnd + lineEnd + "value" + lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size

			bytesAvailable = data.length;
			bufferSize = Math.min(bytesAvailable, maxBufferSize);

			// read file and write it into form...

			dos.write(data, 0, bufferSize);

			// send multipart form data necesssary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			return "";
		}

		catch (IOException ioe) {
			return "";
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuffer b = new StringBuffer();
			String line = null;
			while ((line = reader.readLine()) != null) {
				b.append(line);
			}
			reader.close();
			return b.toString();
		} catch (IOException ioex) {
			return "";
		}

	}

}