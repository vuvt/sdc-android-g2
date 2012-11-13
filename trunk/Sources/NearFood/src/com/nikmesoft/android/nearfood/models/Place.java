package com.nikmesoft.android.nearfood.models;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

@SuppressWarnings("serial")
public class Place implements Serializable {

	private int id;
	private String name;
	private String address;
	private int likedCount;
	private String imagePath;
	private String distance;

	

	public Place() {
	}
	public Place(String name, String address, int likedCount) {
		this.address = address;
		this.name = name;
		this.likedCount = likedCount;
	}

	public Place(int id, String name, String address,
			int likedCount, String imagePath, String distance) {
		this.id = id;
		this.address = address;
		this.name = name;
		this.likedCount = likedCount;
		this.imagePath = imagePath;
		this.distance = distance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLikedCount() {
		return likedCount;
	}
	public void setLikedCount(int likedCount) {
		this.likedCount = likedCount;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}


}
