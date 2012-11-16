package com.nikmesoft.android.nearfood.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.nikmesoft.android.nearfood.models.CheckIn;
import com.nikmesoft.android.nearfood.models.Place;
import com.nikmesoft.android.nearfood.models.User;

public class GetCheckInsOfPlaceHander extends BaseHandler{
	private final static String Checkedin_XML = "item";
	private final static String ID_XML = "id";
	private final static String IDUser_XML = "id_user";
	private final static String FullnameUser_XML = "fullname_user";
	private final static String Image_XML = "image";
	private final static String Description_XML = "description";
	private final static String ChechedInDate_XML = "checkedin_date";
	ArrayList<CheckIn> checkedins = new ArrayList<CheckIn>();
	CheckIn checkin;	
	User user;
	public ArrayList<CheckIn> getPlaces(){
		return checkedins;
				
	}
	@Override
	protected void processNotEndErrorCode(String localName) {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(Checkedin_XML)){
			checkedins.add(checkin);
		}
		else if (localName.equalsIgnoreCase(ID_XML)){
			checkin.setId(Integer.parseInt(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(IDUser_XML)){
			user.setId(Integer.parseInt(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(FullnameUser_XML)){
			user.setFullName(builder.toString().trim());
			checkin.setUser(user);
		}
		else if (localName.equalsIgnoreCase(Image_XML)){
			checkin.setImagePath(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Description_XML)){
			checkin.setDescription(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(ChechedInDate_XML)){
			checkin.setTimeCheck(builder.toString().trim());
		}
		builder.setLength(0);
	}
	@Override
	public void processNotStartErrorCode(String localName) {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(Checkedin_XML)){
			checkin  = new CheckIn();
			user = new User();
		}
	}
	@Override
	protected Object returnNotErrorCode() {
		// TODO Auto-generated method stub
		return checkedins;
	}
}
