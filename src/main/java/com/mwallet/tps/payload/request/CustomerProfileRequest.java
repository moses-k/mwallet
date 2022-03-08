package com.mwallet.tps.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CustomerProfileRequest {
	@NotBlank
	@NotNull
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
