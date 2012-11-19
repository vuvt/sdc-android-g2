package com.nikmesoft.android.nearfood.handlers;

import com.nikmesoft.android.nearfood.MyApplication;

public class ChangePasswordHandler extends BaseHandler{

	String result;
	@Override
	protected void processNotEndErrorCode(String localName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processNotStartErrorCode(String localName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object returnNotErrorCode() {
		// TODO Auto-generated method stub
		return MyApplication.USER_CURRENT;
	}

}
