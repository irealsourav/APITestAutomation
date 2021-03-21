package com.automation.model.response;

import java.util.UUID;

public class CreateUserResp {

	public UUID id;
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String email;
	public String name;

}
