package com.nikmesoft.android.nearfood.handlers;

import com.nikmesoft.android.nearfood.models.User;

public class RegisterHandler extends BaseHandler {
	
	private User user;

	@Override
	protected void processNotEndErrorCode(String localName) {
		if (localName.equalsIgnoreCase("id_user")){
			user.setId(Integer.parseInt(builder.toString().trim()));
		}
		
	}

	@Override
	public void processNotStartErrorCode(String localName) {
		if (localName.equalsIgnoreCase("id_user")) 
			user=new User();
	}

	@Override
	protected Object returnNotErrorCode() {
		if(user!=null){
			return user;
		}
		return null;
	}

}
