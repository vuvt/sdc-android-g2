package com.nikmesoft.android.nearfood.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.android.maps.GeoPoint;
import com.nikmesoft.android.nearfood.models.Place;

public class AddFavoriteHander extends BaseHandler{
	@Override
	protected void processNotEndErrorCode(String localName) {
		// TODO Auto-generated method stub
		
		builder.setLength(0);
	}
	@Override
	public void processNotStartErrorCode(String localName) {
		// TODO Auto-generated method stub
	}
	@Override
	protected Object returnNotErrorCode() {
		// TODO Auto-generated method stub
		return true;
	}
}
