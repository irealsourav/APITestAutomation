package com.automation.model.request;

public class UpdateCar {

	private String manufacture;
	private String model;
	private String imageUrl;

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UpdateCar(String manufacture, String model, String imageUrl) {
		this.manufacture = manufacture;
		this.model = model;
		this.imageUrl = imageUrl;
	}
}
