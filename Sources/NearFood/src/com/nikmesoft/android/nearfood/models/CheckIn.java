package com.nikmesoft.android.nearfood.models;

public class CheckIn {
	private String description, imagePath;
	private long id, idUser, idRestaurant;

	public CheckIn(long id, String description, String imagePath) {
		this.id = id;
		this.description = description;
		this.imagePath = imagePath;
	}

	public CheckIn(long id, long idUser, long idRestaurant, String description,
			String imagePath) {
		this.id = id;
		this.idUser = idUser;
		this.idRestaurant = idRestaurant;
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

	public long getIdUser() {
		return idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}

	public long getIdRestaurant() {
		return idRestaurant;
	}

	public void setIdRestaurant(long idRestaurant) {
		this.idRestaurant = idRestaurant;
	}
}
