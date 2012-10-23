package com.nikmesoft.android.nearfood.models;

import android.annotation.SuppressLint;

@SuppressLint("ParserError")
public class Restaurant {
	private long id;
	private String nameRestaurant;
	private String address;
	private String description;
	private String numberLike;
	private PointMap mapPoint;
	private String imagePath;

	public Restaurant(String nameRestaurant, String address,
			String description, String numberLike) {
		this.address = address;
		this.nameRestaurant = nameRestaurant;
		this.description = description;
		this.numberLike = numberLike;
	}

	public Restaurant(long id, String nameRestaurant, String address,
			String description, String numberLike, PointMap mapPoint,
			String imagePath) {
		this.id = id;
		this.address = address;
		this.nameRestaurant = nameRestaurant;
		this.description = description;
		this.numberLike = numberLike;
		this.mapPoint = mapPoint;
		this.imagePath = imagePath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameRestaurant() {
		return nameRestaurant;
	}

	public void setNameRestaurant(String nameRestaurant) {
		this.nameRestaurant = nameRestaurant;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNumberLike() {
		return numberLike;
	}

	public void setNumberLike(String numberLike) {
		this.numberLike = numberLike;
	}

	public PointMap getMapPoint() {
		return mapPoint;
	}

	public void setMapPoint(PointMap mapPoint) {
		this.mapPoint = mapPoint;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
