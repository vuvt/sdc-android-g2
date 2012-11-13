package com.nikmesoft.android.nearfood.models;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

@SuppressWarnings("serial")
public class Place implements Serializable {

	private long id;
	private String name;
	private String address;
	private String description;
	private int likedCount;
	private GeoPoint mapPoint;
	private String imagePath;
	private String referenceKey;
	private String phoneNumber;

	

	public Place() {
	}

	public Place(String name, String referenceKey) {
		this.name = name;
		this.referenceKey = referenceKey;
	}

	public Place(String name, String address, String description, int likedCount) {
		this.address = address;
		this.name = name;
		this.description = description;
		this.likedCount = likedCount;
	}

	public Place(long id, String name, String address, String description,
			int likedCount, GeoPoint mapPoint, String imagePath) {
		this.id = id;
		this.address = address;
		this.name = name;
		this.description = description;
		this.likedCount = likedCount;
		this.mapPoint = mapPoint;
		this.imagePath = imagePath;
	}

	public long getId() {
		return id;
	}

	public Place setId(long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Place setNamePlace(String name) {
		this.name = name;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Place setAddress(String address) {
		this.address = address;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Place setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getLikedCount() {
		return likedCount;
	}

	public Place setLikedCount(int likedCount) {
		this.likedCount = likedCount;
		return this;
	}

	public GeoPoint getMapPoint() {
		return mapPoint;
	}

	public Place setMapPoint(GeoPoint mapPoint) {
		this.mapPoint = mapPoint;
		return this;
	}

	public String getImagePath() {
		return imagePath;
	}

	public Place setImagePath(String imagePath) {
		this.imagePath = imagePath;
		return this;
	}

	public String getReferenceKey() {
		return referenceKey;
	}

	public Place setReferenceKey(String referenceKey) {
		this.referenceKey = referenceKey;
		return this;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Place setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
}
