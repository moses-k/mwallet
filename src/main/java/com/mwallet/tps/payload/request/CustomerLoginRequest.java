package com.mwallet.tps.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CustomerLoginRequest {
	
	@NotBlank
	@NotNull
	private String email;
	@NotBlank
	@NotNull
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
