package com.nikmesoft.android.nearfood.models;

import java.io.Serializable;

import android.annotation.SuppressLint;

@SuppressLint("ParserError")
public class Place implements Serializable{

	private long id;
	private String name;
	private String address;
	private String description;
	private String likedCount;
	private PointMap mapPoint;
	private String imagePath;

	public Place(String name, String address,
			String description, String likedCount) {
		this.address = address;
		this.name = name;
		this.description = description;
		this.likedCount = likedCount;
	}

	public Place(long id, String name, String address,
			String description, String likedCount, PointMap mapPoint,
			String imagePath) {
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

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setNamePlace(String name) {
		this.name = name;
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

	public String getLikedCount() {
		return likedCount;
	}

	public void setLikedCount(String likedCount) {
		this.likedCount = likedCount;
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
