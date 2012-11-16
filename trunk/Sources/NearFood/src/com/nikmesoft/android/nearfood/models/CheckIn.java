package com.nikmesoft.android.nearfood.models;

public class CheckIn {
        private String description, imagePath;
        private int id;
        private User user;
        private Place place;
        private String TimeCheck;
        public CheckIn(){
                
        }
        
        public CheckIn(int id, String description, String imagePath) {
                this.id = id;
                this.description = description;
                this.imagePath = imagePath;
        }

        public CheckIn(int id, User user, Place place, String description,
                        String imagePath) {
                this.id = id;
                this.user = user;
                this.place = place;
                this.description = description;
                this.imagePath = imagePath;
        }
        public CheckIn(int id, User user, Place place, String description,
                String imagePath, String TimeCheck) {
        this.id = id;
        this.user = user;
        this.place = place;
        this.description = description;
        this.imagePath = imagePath;
        this.TimeCheck = TimeCheck;
}

        public String getTimeCheck() {
			return TimeCheck;
		}

		public void setTimeCheck(String timeCheck) {
			TimeCheck = timeCheck;
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

        public void setId(int id) {
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