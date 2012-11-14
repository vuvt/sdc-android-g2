package com.nikmesoft.android.nearfood.handlers;



import com.nikmesoft.android.nearfood.models.User;


public class LoginHandler extends BaseHandler {
	private final String USER="User";
	private User user=null;
	
	@Override
	protected void processNotEndErrorCode(String localName) {
		if (localName.equalsIgnoreCase("id")){
			user.setId(Long.parseLong(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase("fullname")){
			user.setFullName(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase("email")){
			user.setEmail(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase("gender")){
			user.setGender(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase("birthday")){
			user.setBirthday(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase("avatar")){
			user.setProfilePicture(builder.toString().trim());
		}
			
	}
	@Override
	protected Object returnNotErrorCode() {
		if(user!=null){
			return user;
		}
		return null;
	}
	@Override
	public void processNotStartErrorCode(String localName) {
		if (localName.equalsIgnoreCase(USER)) 
			user=new User();
	}
}
