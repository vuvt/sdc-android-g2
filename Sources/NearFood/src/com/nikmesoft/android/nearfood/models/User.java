package com.nikmesoft.android.nearfood.models;

public class User {
	private String fullName, email, birthday, profilePicture;
	private long id;

	public User(String fullName, String email, String birthday,
			String profilePicture) {
		this.fullName = fullName;
		this.email = email;
		this.birthday = birthday;
		this.profilePicture = profilePicture;
	}

	public User(long id, String fullName, String email, String birthday,
			String profilePicture) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.birthday = birthday;
		this.profilePicture = profilePicture;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
