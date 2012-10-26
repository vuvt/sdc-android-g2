package com.nikmesoft.android.nearfood.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class Utility {
	static int IO_BUFFER_SIZE = 1024;

	public static Drawable LoadImageFromWebOperations(String url, String srcName)
			throws MalformedURLException, IOException {
		InputStream is = (InputStream) new URL(url).getContent();
		Drawable d = Drawable.createFromStream(is, srcName);
		return d;

	}

}
