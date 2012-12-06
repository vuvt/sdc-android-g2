package com.nikmesoft.android.nearfood.handlers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.nikmesoft.android.nearfood.models.Place;

public class GetPlaceHander extends BaseHandler{
	private final static String Place_XML = "item";
	private final static String ID_XML = "id";
	private final static String Name_XML = "name";
	private final static String Address_XML = "address";
	private final static String Like_XML = "like"; 
	private final static String IsLiked_XML = "is_liked"; 
	private final static String IsFavorited_XML = "is_favorited"; 
	private final static String Reference_XML = "reference_key";
	private final static String Phone_XML = "phone";
	private final static String Distance_XML = "distance";
	private final static String Image_XML = "image", Description_XML="description";
	private final static String Longitude_XML="longitude";
	private final static String Latitude_XML="latitude";
	private final static String TotalPage_XML="TotalPage";
	ArrayList<Place> places = new ArrayList<Place>();
	Place place;	
	int longitude, latitude;
	GeoPoint geopoint;
	public ArrayList<Place> getPlaces(){
		return places;
				
	}
	@Override
	protected void processNotEndErrorCode(String localName) {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(Place_XML)){
			places.add(place);
		}
		else if (localName.equalsIgnoreCase(ID_XML)){
			place.setId(Integer.parseInt(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(Name_XML)){
			place.setNamePlace(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Address_XML)){
			place.setAddress(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Like_XML)){
			place.setLikedCount(Integer.parseInt(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(Distance_XML)){
			place.setDistance(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Image_XML)){
			place.setImagePath(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Reference_XML )){
			place.setReferenceKey(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Phone_XML)){
			place.setPhoneNumber(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Description_XML)){
			place.setDescription(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(IsLiked_XML)){
			place.setLiked(Boolean.parseBoolean(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(IsFavorited_XML)){
			place.setFavorited(Boolean.parseBoolean(builder.toString().trim()));
		}
		else if (localName.equalsIgnoreCase(Latitude_XML)){
			latitude = (int)(Double.parseDouble(builder.toString().trim()) * 1E6);
		}
		else if (localName.equalsIgnoreCase(TotalPage_XML)){
			toTalPage = Integer.parseInt(builder.toString().trim());
		}
		else if (localName.equalsIgnoreCase(Longitude_XML)){
			longitude = (int)(Double.parseDouble(builder.toString().trim()) * 1E6);
			geopoint = new GeoPoint(latitude, longitude);
			place.setMapPoint(geopoint);
		}
		
		builder.setLength(0);
	}
	@Override
	public void processNotStartErrorCode(String localName) {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(Place_XML)){
			place  = new Place();
			longitude = latitude = 0;
		}
	}
	@Override
	protected Object returnNotErrorCode() {
		// TODO Auto-generated method stub
		return places;
	}
}
