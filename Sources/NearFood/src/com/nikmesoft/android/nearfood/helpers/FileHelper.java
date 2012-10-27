package com.nikmesoft.android.nearfood.helpers;

import java.io.File;
import com.nikmesoft.android.nearfood.MyApplication;
import android.content.Context;
import android.os.Environment;

public class FileHelper {
	private static FileHelper instance;
	private String root;

	public FileHelper(Context context) {
		super();
		root = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/" + MyApplication.FOLDER_NAME + "/";
		checkAppFolder();
	}

	public static void initInstance(Context context) {
		if (instance == null) {
			// Create the instance
			instance = new FileHelper(context);
		}
	}

	public static FileHelper getInstance() {
		// Return the instance
		return instance;
	}

	private void checkAppFolder() {
		File file = new File(root);
		if (!file.exists()) {
			file.mkdir();
		} else {
			if (file.isFile()) {
				file.delete();
				file.mkdir();
			}
		}
	}

	public boolean addFolder(String path) {
		String fullpath = root + path;
		File folder = new File(fullpath);
		if (!folder.exists()) {
			return folder.mkdir();
		} else {
			return true;
		}
	}
}
