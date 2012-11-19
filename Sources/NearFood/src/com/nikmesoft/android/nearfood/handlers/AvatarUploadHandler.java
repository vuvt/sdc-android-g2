package com.nikmesoft.android.nearfood.handlers;

import android.util.Log;

public class AvatarUploadHandler extends BaseHandler {
	public String newAvatar;

	@Override
	protected void processNotEndErrorCode(String localName) {
		if (localName.equalsIgnoreCase("newAvatar")){
			newAvatar = builder.toString().trim();
		}
	}

	@Override
	public void processNotStartErrorCode(String localName) {
		//nothing...
	}

	@Override
	protected Object returnNotErrorCode() {
		//nothing...
		Log.d("KADHJSFJKSBDJKS", newAvatar);
		return (Object)newAvatar;
	}

}
