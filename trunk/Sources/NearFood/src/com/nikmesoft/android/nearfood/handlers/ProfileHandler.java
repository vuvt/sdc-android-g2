package com.nikmesoft.android.nearfood.handlers;

import com.nikmesoft.android.nearfood.MyApplication;
import com.nikmesoft.android.nearfood.models.User;

public class ProfileHandler extends BaseHandler {

	@Override
	protected void processNotEndErrorCode(String localName) {
		//nothing...
	}

	@Override
	public void processNotStartErrorCode(String localName) {
		//nothing...
	}

	@Override
	protected Object returnNotErrorCode() {
		//nothing...
		return MyApplication.USER_CURRENT;
	}

}
