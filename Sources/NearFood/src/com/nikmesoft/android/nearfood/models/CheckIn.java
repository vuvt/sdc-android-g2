package com.nikmesoft.android.nearfood.models;

public class CheckIn {
	private String description, imagePath;
	private long id;
	private User user;
	private Place place;
	
	public CheckIn(){
		
	}
	
	public CheckIn(long id, String description, String imagePath) {
		this.id = id;
		this.description = description;
		this.imagePath = imagePath;
	}

	public CheckIn(long id, User user, Place place, String description,
			String imagePath) {
		this.id = id;
		this.user = user;
		this.place = place;
		this.description = description;
		this.imagePath = imagePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}
}
