package com.automation.model.request;

import java.util.UUID;

public class CreateCar {
	private String manufacture;
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

	private String model;
	private String imageUrl;
	private UUID userId;
	
	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public CreateCar(String manufacture,String model,String imageUrl,UUID userId)
	{
		this.manufacture=manufacture;
		this.model=model;
		this.imageUrl=imageUrl;
		this.userId=userId;
		
	}


}
